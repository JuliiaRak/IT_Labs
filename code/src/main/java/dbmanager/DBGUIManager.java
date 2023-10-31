package dbmanager;

import database.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class DBGUIManager extends JFrame {
    private Database database;

    private final JLabel dbNameLabel = new JLabel("Please open or create your Database");
    private final JLabel centralPanelMainLabel = new JLabel("MAIN PROGRAM INTERFACE");
    private final JLabel databaseOperationsLabel = new JLabel("Database operations");
    private final JList tableList = new JList();
    private final DefaultListModel tableListModel = new DefaultListModel();
    private final JLabel resultMessage = new JLabel();
    private final MyTableModel resultTableModel = new MyTableModel();
    private final JPanel tableControlPanel = new JPanel();
    private final JFileChooser fileChooser = new JFileChooser();

    JPanel CentralPanel = new JPanel();

    DBGUIManager() {
        super("Julia Rak");

        Font newFont = new Font("Times New Roman", Font.BOLD, 18);
        JLabel label = new JLabel("<html><body><p style='color:blue; font-size:10px;'>Styled Text</p></body></html>");

        dbNameLabel.setFont(newFont);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1600, 1000);
        this.getContentPane().setBackground(new Color(107, 106, 104));
        this.getContentPane().setFont(newFont);

        this.getContentPane().setBackground(Color.green);
//        this.getContentPane().add(BorderLayout.NORTH, initializeMenuBar()).setBackground(new Color(156, 187, 214)); // File Operations Help
        this.getContentPane().add(BorderLayout.WEST, initializeMenuPanel()).setBackground(new Color(167, 199, 149));
        this.getContentPane().add(BorderLayout.CENTER, initializeResultPanel()).setBackground(new Color(216, 178, 232));
        ;;

        Font centralTextFont = new Font("Times New Roman", Font.BOLD, 50);

        // Текст над трьома головними кнопками
        centralPanelMainLabel.setFont(centralTextFont);
        centralPanelMainLabel.setForeground(new Color(135, 123, 243));

        // ТРИ ГОЛОВНІ КНОПКИ. Open DB   |    Save DB
        JButton OpenDatabaseButton = ButtonsManager.GetOpenDatabaseButton();
        OpenDatabaseButton.addActionListener(e -> {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String databasePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    database = new DatabaseReader(databasePath).read();
                } catch (Exception ex) {
                    database = new Database(databasePath);
                    ex.printStackTrace();
                }
                dbNameLabel.setText(fileChooser.getSelectedFile().getName());

                populateTableList();
            }
        });
        JButton SaveDatabaseAsButton = ButtonsManager.getSaveDatabaseAsButton();
        SaveDatabaseAsButton.addActionListener(e -> {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                database.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    database.save();
                    dbNameLabel.setText(fileChooser.getSelectedFile().getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton createDBButton = ButtonsManager.getCreateDBButton();
        createDBButton.addActionListener(e -> {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String newDatabasePath = fileChooser.getSelectedFile().getAbsolutePath();

                // Перевірка, чи має файл розширення .json. Якщо ні - додаємо його.
                if (!newDatabasePath.endsWith(".json")) {
                    newDatabasePath += ".json";
                }

                File newDatabaseFile = new File(newDatabasePath);
                if (!newDatabaseFile.exists()) {
                    try {
                        newDatabaseFile.createNewFile();
                        // Ініціалізація нової бази даних та запис її в файл.
                        database = new Database(newDatabasePath);
                        database.save();
                        dbNameLabel.setText(newDatabaseFile.getName());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // Тут можна вивести повідомлення про те, що файл вже існує, і запитати, чи хоче користувач його перезаписати.
                    System.out.println("File already exists!");
                }
            }
        });

        // створюємо окрему панель для двох головних кнопок. щоб вони були разом на одному рівні
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(createDBButton);
        buttonPanel.add(OpenDatabaseButton);
        buttonPanel.add(SaveDatabaseAsButton);

//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50,10,10,0));

        // Розміщення текстового компонента та панелі кнопок на головному вікні
        CentralPanel.add(centralPanelMainLabel, BorderLayout.CENTER);  // Тут titleLabel - це ваш компонент із текстом "MAIN PROGRAM INTERFACE"
        CentralPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ___________

        // Текст для трьох кнопок DB Operationss
        databaseOperationsLabel.setFont(centralTextFont);
        databaseOperationsLabel.setForeground(new Color(255, 0, 230));

        // 3 КНОПКИ Database Operations
        JButton CreateDBTableButton = ButtonsManager.getCreateDBTableButton();
        CreateDBTableButton.addActionListener(e -> {
            if (database == null) {
                JOptionPane.showMessageDialog(this, "Please choose Database");
                return;
            }
            TableAddPanel tableAdd = new TableAddPanel();
            if (JOptionPane.showConfirmDialog(this, tableAdd,
                    "Please enter table content:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                displayQueryResults(database.query(tableAdd.getDBQuery()), false);
                populateTableList();
            }
        });

        JButton RemoveDBTableButton = ButtonsManager.getRemoveDBTableButton();
        RemoveDBTableButton.addActionListener(e -> {
            String tableName = (String) tableList.getSelectedValue();
            if (tableName == null || database == null) {
                JOptionPane.showMessageDialog(this, "Table isn't chosen!");
                return;
            }
            if (JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove " + tableName + "?", "",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    displayQueryResults(database.query("REMOVE TABLE " + tableName), false);
                    populateTableList();
                } catch (Exception e1) {
                    System.err.println("Error: " + e1.getMessage());
                }
            }
        });

        JButton renameButton = ButtonsManager.getTableRenameButton();
        renameButton.addActionListener(e -> {
            String tableName = (String) tableList.getSelectedValue();  // Отримання вибраної таблиці
            List<String> columnNames = null;

            try {
                columnNames = database.getTableColumns(tableName)
                        .stream()
                        .map(Column::getName)
                        .collect(Collectors.toList());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            JComboBox<String> columnDropdown = new JComboBox<>(columnNames.toArray(new String[0]));
            JTextField newNameField = new JTextField(10);

            JPanel panel = new JPanel();
            panel.add(columnDropdown);
            panel.add(new JLabel("New Name:"));
            panel.add(newNameField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Rename/Rearrange Column", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String selectedColumn = (String) columnDropdown.getSelectedItem();
                String newName = newNameField.getText();

                displayQueryResults(database.query(
                    String.format("ALTER TABLE %s RENAME COLUMN %s TO %s", tableName, selectedColumn, newName)), false);
            }


        });

        JButton rearrangeButton = ButtonsManager.getTableRearrangeButton();
        rearrangeButton.addActionListener(e -> {
            String tableName = (String) tableList.getSelectedValue();  // Отримання вибраної таблиці
            if (tableName == null || database == null) {
                JOptionPane.showMessageDialog(this, "Table isn't chosen!");
                return;
            }

            List<String> columnNames = null;
            try {
                columnNames = database.getTableColumns(tableName)
                        .stream()
                        .map(Column::getName)
                        .collect(Collectors.toList());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            // Dialog for rearranging columns
            JComboBox<String> columnToMoveBox = new JComboBox<>(columnNames.toArray(new String[0]));
            JComboBox<String> moveToPositionBox = new JComboBox<>(columnNames.toArray(new String[0]));

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel("Move Column:"));
            panel.add(columnToMoveBox);
            panel.add(new JLabel("To Position Before:"));
            panel.add(moveToPositionBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Rearrange Columns", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String columnToMove = (String) columnToMoveBox.getSelectedItem();
                String moveToPosition = (String) moveToPositionBox.getSelectedItem();

                // For now, we just display the user's choice. Actual logic to rearrange columns will be added later.
                System.out.println("Moving column " + columnToMove + " to position before " + moveToPosition);

                try {
                    database.rearrangeColumn(tableName, columnToMove, moveToPosition);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // створюємо окрему панель для трьох DB Operations кнопок. щоб вони були разом на одному рівні
        JPanel dbOperationsDBButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dbOperationsDBButtonPanel.add(CreateDBTableButton);  // Тут openDatabaseButton - це ваша кнопка "Open Database"
        dbOperationsDBButtonPanel.add(RemoveDBTableButton);
        dbOperationsDBButtonPanel.add(renameButton);
        dbOperationsDBButtonPanel.add(rearrangeButton);

        // Розміщення текстового компонента та панелі кнопок на головному вікні
        CentralPanel.add(databaseOperationsLabel, BorderLayout.CENTER);  // Тут titleLabel - це ваш компонент із текстом "MAIN PROGRAM INTERFACE"
        CentralPanel.add(dbOperationsDBButtonPanel, BorderLayout.CENTER);

//        CentralPanel.add(BorderLayout.NORTH, dbNameLabel);
        CentralPanel.setBackground(new Color(199,255,237));
        CentralPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 100, 0));
        this.add(CentralPanel, BorderLayout.NORTH);
    }

//    private JMenuBar initializeMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//        JMenu menuFile = new JMenu("File");
//        JMenu menuAction = new JMenu("Operations");
//        JMenu menuHelp = new JMenu("Help");
//        menuBar.add(menuFile);
//        menuBar.add(menuAction);
//        menuBar.add(menuHelp);
//        JMenuItem menuFileOpen = new JMenuItem("Open Database");
//        menuFileOpen.addActionListener(e -> {
//            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
//            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                String databasePath = fileChooser.getSelectedFile().getAbsolutePath();
//                try {
//                    database = new DatabaseReader(databasePath).read();
//                } catch (Exception ex) {
//                    database = new Database(databasePath);
//                    ex.printStackTrace();
//                }
//                dbNameLabel.setText(fileChooser.getSelectedFile().getName());
//                populateTableList();
//            }
//        });
//        JMenuItem menuFileSaveAs = new JMenuItem("Save as");
//        menuFileSaveAs.addActionListener(e -> {
//            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
//            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//                database.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
//                try {
//                    database.save();
//                    dbNameLabel.setText(fileChooser.getSelectedFile().getName());
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//        menuFile.add(menuFileOpen);
//        menuFile.add(menuFileSaveAs);
//        JMenuItem menuCreateTable = new JMenuItem("Create DB table");
//        JMenuItem menuDeleteTable = new JMenuItem("Remove DB table");
//        JMenuItem menuSubtract = new JMenuItem("Tables difference");
//        menuCreateTable.addActionListener(e -> {
//            if (database == null) {
//                JOptionPane.showMessageDialog(this, "Please choose Database");
//                return;
//            }
//            TableAddPanel tableAdd = new TableAddPanel();
//            if (JOptionPane.showConfirmDialog(this, tableAdd,
//                    "Please enter table content:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                displayQueryResults(database.query(tableAdd.getDBQuery()), false);
//                populateTableList();
//            }
//        });
//        menuDeleteTable.addActionListener(e -> {
//            String tableName = (String) tableList.getSelectedValue();
//            if (tableName == null || database == null) {
//                JOptionPane.showMessageDialog(this, "Table isn't chosen!");
//                return;
//            }
//            if (JOptionPane.showConfirmDialog(this,
//                    "Are you sure you want to remove " + tableName + "?", "",
//                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                        try {
//                displayQueryResults(database.query("REMOVE TABLE " + tableName), false);
//                populateTableList();
//            } catch (Exception e1) {
//                System.err.println("Error: " + e1.getMessage());
//            }
//            }
//        });
//        menuSubtract.addActionListener(e -> {
//            java.util.List tableNames = tableList.getSelectedValuesList();
//            if (tableNames.size() != 2 || database == null) {
//                JOptionPane.showMessageDialog(this, "You need to chose 2 tables!");
//                return;
//            }
//            JOptionPane.showMessageDialog(this, "Difference " + tableNames.get(0) + " and " + tableNames.get(1));
//            displayQueryResults(database.query(
//                    String.format("Subtract %s from %s", tableNames.get(0), tableNames.get(1))), false);
//            JOptionPane.showMessageDialog(this, "Різниця " + tableNames.get(1) + " і " + tableNames.get(0));
//            displayQueryResults(database.query(
//                    String.format("Subtract %s from %s", tableNames.get(1), tableNames.get(0))), false);
//        });
//
//        menuAction.add(menuCreateTable);
//        menuAction.add(menuDeleteTable);
//        menuAction.add(menuSubtract);
//        JMenuItem menuAbout = new JMenuItem("About");
//        menuAbout.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Julia Rak");
//        });
//        menuHelp.add(menuAbout);
//        return menuBar;
//    }

    private JPanel initializeMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        tableList.setModel(tableListModel);
        tableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableList.setLayoutOrientation(JList.VERTICAL);
        tableList.setVisibleRowCount(-1);
        tableList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = tableList.locationToIndex(e.getPoint());
                    String tableName = tableListModel.getElementAt(index).toString();
                    displayQueryResults(database.query("select * from " + tableName), true);
                }
            }
        });
        JScrollPane listScroll = new JScrollPane(tableList);
        listScroll.setPreferredSize(new Dimension(300, 100));
        menuPanel.add(BorderLayout.NORTH, dbNameLabel);
        menuPanel.add(BorderLayout.CENTER, listScroll);
        return menuPanel;
    }

    private JPanel initializeResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(BorderLayout.NORTH, resultMessage);
        JTable resultTable = new JTable(resultTableModel);

        Font initialFont = new Font("Arial", Font.PLAIN, 28); // встановлення фіксованого розміру шрифту
        resultTable.setFont(initialFont);

        resultTable.setForeground(new Color(62, 83, 171));
        resultTable.setBackground(new Color(233, 253, 252));

        JScrollPane tableScroll = new JScrollPane(resultTable);
        resultTable.setFillsViewportHeight(true);
        resultPanel.add(BorderLayout.CENTER, tableScroll);

        resultTable.setRowHeight(60); // Задаємо висоту рядка, наприклад, 30 пікселів
//        TableColumnModel columnModel = resultTable.getColumnModel();
//        for (int i = 0; i < columnModel.getColumnCount(); i++) {
//            TableColumn column = columnModel.getColumn(i);
//            column.setPreferredWidth(100); // Задаємо ширину стовпця, наприклад, 100 пікселів
//        }

        JButton addRowButton = ButtonsManager.getAddRowButton();
        addRowButton.addActionListener(e -> {
            if (database == null) {
                JOptionPane.showMessageDialog(this, "No open database");
                return;
            }
            String tableName = (String) tableList.getSelectedValue();
            try {
                RowAddPanel rowAdd = new RowAddPanel(tableName, database.getTableColumns(tableName));
                if (JOptionPane.showConfirmDialog(this, rowAdd,
                        "Enter data for the new row:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    displayQueryResults(database.query(rowAdd.getDBQuery()), false);
                    populateTableList();
                }
            } catch (Exception ex) {
            }
        });
        tableControlPanel.add(addRowButton);
        tableControlPanel.setVisible(false);
        resultPanel.add(BorderLayout.SOUTH, tableControlPanel);
        return resultPanel;
    }

    private void populateTableList() {//
        Font customFont = new Font("Arial", Font.BOLD, 25); // Приклад налаштування шрифту
        tableList.setFont(customFont);
        tableList.setBackground(new Color(229, 206 ,233));
        tableList.setForeground(new Color(0, 22 ,166));
//
        tableList.setModel(tableListModel);

        if (database != null) {
            Result tables = database.query("list tables");
            tableListModel.clear();
            for (Row row : tables.getRows()) {
                tableListModel.addElement(row.getElement("table_name").getValue());
            }
        }
    }

    private void displayQueryResults(Result result, boolean isTableDisplayed) {
        tableControlPanel.setVisible(isTableDisplayed);

//        resultMessage.setText("<html>Result: " + result.getStatus() +
//                (result.getStatus() == Result.Status.FAIL ? "<br/>" + result.getReport() : "") +
//                (result.getRows() == null || result.getRows().size() == 0 ? "<br/>Result rows empty" : "") +
//                "</html>");
        resultTableModel.setResult(result);
    }
}

class MyTableModel extends AbstractTableModel {
    ArrayList<Row> rows = new ArrayList<>();
    ArrayList<String> columns = new ArrayList<>();

    void setResult(Result result) {
        rows.clear();
        columns.clear();
        if (result.getRows() != null && !result.getRows().isEmpty()) {
            rows.addAll(result.getRows());
            for (Element element : result.getRows().iterator().next().getElements()) {
                columns.add(element.getColumn());
            }
        }
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getElement(columns.get(columnIndex)).getValue();
    }

    @Override
    public String getColumnName(int column) {
        return columns.get(column);
    }
}