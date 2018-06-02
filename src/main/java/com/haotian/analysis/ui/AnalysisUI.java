package com.haotian.analysis.ui;

import com.haotian.analysis.ThreadInfo;
import com.haotian.analysis.ui.config.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AnalysisUI {
    private static final Config CONFIG = ConfigUtil.getDefaultConfig();

    public static void main(String[] args) throws Exception {
        JFrame workspace = new JFrame();
        workspace.setTitle(CONFIG.getTitle());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Screen screen = CONFIG.getScreen(screenSize.width, screenSize.height);
        int workspaceWidth = Math.max(CONFIG.getMinWidth(), screenSize.width / 2);
        int workspaceHeight = Math.max(CONFIG.getMinHeight(), screenSize.height / 2);
        workspace.setSize(workspaceWidth, workspaceHeight);
        workspace.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Font workspaceFont = new Font("Default", Font.PLAIN, screen.getFontSize());
        workspace.setJMenuBar(createJMenuBar(workspace, workspaceFont));
        workspace.getContentPane().add(createWorkspaceArea(screen, workspaceWidth, workspaceHeight, workspaceFont), BorderLayout.CENTER);
        setUIFont(workspaceFont);
        workspace.setVisible(true);
    }

    private static JSplitPane createWorkspaceArea(Screen screen, int workspaceWidth, int workspaceHeight, Font menuFont) {
        JTable table = new JTable();
        ComposeHolder.table = table;
        table.setFont(menuFont);
        table.getTableHeader().setFont(menuFont);
        ThreadHeaderMouseListener threadHeaderMouseListener = new ThreadHeaderMouseListener();
        table.getTableHeader().addMouseListener(threadHeaderMouseListener);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRowCount() > 1) {
                    ComposeHolder.textPane.setText("selected [" + table.getSelectedRowCount() + "] lines");
                } else {
                    ComposeHolder.textPane.setText(((ThreadTableModel) table.getModel()).getValueAt(e.getFirstIndex()).getThreadDetails());
                }
            }
        });
        table.setRowHeight(screen.getFontSize() + 10);
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createNavTree(menuFont));
        splitPane.setDividerLocation(workspaceWidth / 3);
        JScrollPane scrollTable = new JScrollPane();
        scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTable.getViewport().add(table);
        JSplitPane contentSplitPane = new JSplitPane();
        contentSplitPane.setDividerLocation(workspaceHeight / 2);
        contentSplitPane.setTopComponent(scrollTable);
        JTextPane textPane = new JTextPane();
        ComposeHolder.textPane = textPane;
        JScrollPane scrollText = new JScrollPane();
        scrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollText.getViewport().add(textPane);
        textPane.setFont(menuFont);
        contentSplitPane.setBottomComponent(scrollText);
        contentSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JPanel queryPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        queryPanel.setLayout(flowLayout);
        JLabel queryLabel = new JLabel(CONFIG.getMenu().get("label-condition"));
        queryLabel.setFont(menuFont);
        queryPanel.add(queryLabel);
        JTextField queryText = new JTextField(20);
        queryText.setFont(menuFont);
        ComposeHolder.queryArg = queryText;
        queryPanel.add(queryText);

        JComboBox<Item> queryTypeCombox = new JComboBox<Item>(CONFIG.getQueryType());
        ComposeHolder.queryType = queryTypeCombox;
        queryTypeCombox.setFont(menuFont);
        queryPanel.add(queryTypeCombox);
        JButton doQueryButton = new JButton(CONFIG.getMenu().get("action-query"));
        doQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                threadHeaderMouseListener.doThreadTableReload();
            }
        });
        doQueryButton.setFont(menuFont);
        queryPanel.add(doQueryButton);
        queryPanel.setFont(menuFont);
        centerPanel.add(queryPanel, BorderLayout.NORTH);
        centerPanel.add(contentSplitPane, BorderLayout.CENTER);
        splitPane.setRightComponent(centerPanel);
        return splitPane;
    }

    private static void setUIFont(Font f)
    {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
            {
                UIManager.put(key, f);
            }
        }
    }

    private static JTree createNavTree(Font menuFont) {
        JTree tree = new JTree();
        ComposeHolder.tree = tree;
        tree.setFont(menuFont);
        ThreadTreeModel treeModel = new ThreadTreeModel();
        tree.setModel(treeModel);
        tree.setBorder(new EmptyBorder(10,15,10,15));
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                ThreadSummary selectedItem = (ThreadSummary) e.getPath().getLastPathComponent();
                if (selectedItem == null) {
                    return;
                }

                ThreadTableModel tableModel = new ThreadTableModel(selectedItem.getThreadEncId(), -1, ThreadTableModel.ASC, ComposeHolder.queryArg.getText(), ComposeHolder.byQuery());
                ComposeHolder.table.setModel(tableModel);
                ComposeHolder.table.setEnabled(true);
            }
        });
        /*DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) (tree.getCellRenderer());
        render.setLeafIcon(LeafIcon);
        render.setClosedIcon(ClosedIcon);
        render.setOpenIcon(OpenedIcon);*/
        return tree;
    }

    private static JMenuBar createJMenuBar(JFrame workspace, Font menuFont) {
        JMenu fileMenu = new JMenu(CONFIG.getMenu().get("menu-file"));
        fileMenu.setFont(menuFont);

        JMenuItem openMenuItem = new JMenuItem(CONFIG.getMenu().get("menu-open"));
        openMenuItem.setFont(menuFont);
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (ComposeHolder.fileChooser == null) {
                    JFileChooser jfc=new JFileChooser();
                    jfc.setFont(menuFont);
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
                    jfc.setFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return CONFIG.support(f);
                        }

                        @Override
                        public String getDescription() {
                            return null;
                        }
                    });
                    ComposeHolder.fileChooser = jfc;
                }
                ComposeHolder.fileChooser.showDialog(new JLabel(), CONFIG.getMenu().get("menu-select"));
                File hsfStackFile=ComposeHolder.fileChooser.getSelectedFile();
                if (hsfStackFile == null) {
                    return;
                }

                boolean isThreadDump = isThreadDump(hsfStackFile);

                List<ThreadInfo> threadInfoList = isThreadDump ? parseDumpThreadInfo(hsfStackFile) : parseHsfThreadInfo(hsfStackFile);
                H2Helper.saveThreadSummary(hsfStackFile.getName(), hsfStackFile.getName());
                for (ThreadInfo threadInfo: threadInfoList) {
                    threadInfo.initDefault();
                    H2Helper.saveThreadItem(new ThreadItem(hsfStackFile.getName(), threadInfo.getThreadName(), threadInfo.getStackStrace(), threadInfo.getThreadDecId(), threadInfo.getThreadState(), threadInfo.getThreadHexId()));
                }
                ComposeHolder.tree.setModel(new ThreadTreeModel());
            }

            private boolean isThreadDump(File hsfStackFile) {
                boolean isThreadDump = false;
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(hsfStackFile)));
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        if (line.contains("Full thread dump")) {
                            isThreadDump = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("read thread file[" + hsfStackFile.getName() + "] error", e);
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        // do nothing
                    }
                }
                return isThreadDump;
            }

            private List<ThreadInfo> parseDumpThreadInfo(File hsfStackFile) {
                List<ThreadInfo> threadInfoList = new ArrayList<>();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(hsfStackFile)));
                    ThreadInfo threadInfo = null;
                    boolean isBeginParse = false;
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        if (!isBeginParse && line.contains("Full thread dump")) {
                            isBeginParse = true;
                            continue;
                        }
                        if (line.contains(" prio=") && line.contains(" nid=")) {
                            threadInfo = new ThreadInfo();
                            int bidx = line.indexOf("\"") + 1;
                            int eidx = line.indexOf("\"", bidx);
                            threadInfo.setThreadName("[" + line.substring(bidx, eidx) + "]");

                            bidx = line.indexOf("nid=0x") + "nid=0x".length();
                            eidx = line.indexOf(" ", bidx);
                            threadInfo.setThreadHexId(line.substring(bidx, eidx));
                            threadInfo.setThreadDecId(Integer.toString(Integer.parseInt(threadInfo.getThreadHexId(), 16)));
                            threadInfoList.add(threadInfo);
                        } else if (line.contains("java.lang.Thread.State: ")) {
                            if (threadInfo != null) {
                                int bidx = line.indexOf("java.lang.Thread.State: ") + "java.lang.Thread.State: ".length();
                                int edix = line.indexOf(" ", bidx);
                                if (edix == -1) {
                                    edix = line.length();
                                }
                                threadInfo.setThreadState(line.substring(bidx, edix));
                            }
                        } else if (line.length() == 0) {
                            continue;
                        } else if (line.contains("JNI global references")) {
                            break;
                        }
                        if (threadInfo != null) {
                            threadInfo.addStackTrace(line);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("read thread file[" + hsfStackFile.getName() + "] error", e);
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        // do nothing
                    }
                }
                return threadInfoList;
            }

            private List<ThreadInfo> parseHsfThreadInfo(File hsfStackFile) {
                List<ThreadInfo> threadInfoList = new ArrayList<>();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(hsfStackFile)));
                    ThreadInfo threadInfo = null;
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        if (line.startsWith("Thread Name :[")) {
                            threadInfo = new ThreadInfo();
                            threadInfo.setThreadName(line);
                            threadInfoList.add(threadInfo);
                        } else if (line.length() == 0) {
                            continue;
                        }
                        if (threadInfo != null) {
                            threadInfo.addStackTrace(line);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("read thread file[" + hsfStackFile.getName() + "] error", e);
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        // do nothing
                    }
                }
                return threadInfoList;
            }
        });
        fileMenu.add(openMenuItem);


        JMenuItem exitMenuItem = new JMenuItem(CONFIG.getMenu().get("menu-exit"));
        exitMenuItem.setFont(menuFont);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                workspace.dispose();
            }
        });
        fileMenu.add(exitMenuItem);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(menuFont);
        menuBar.add(fileMenu);
        return menuBar;
    }
}
