package ru.vsu.cs.course1.game;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.vsu.cs.util.DrawUtils;
import ru.vsu.cs.util.JTableUtils;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JTable tableGameField;
    private JLabel labelStatus;

    private static final int DEFAULT_COL_COUNT = 11;
    private static final int DEFAULT_ROW_COUNT = 11;
    private static final int DEFAULT_COLOR_COUNT = 6;
    private static final int DEFAULT_GAP = 8;
    private static final int DEFAULT_CELL_SIZE = 70;
    private static int attempt = 0;
    private static int finaltime;
    private static boolean GameOver = false;

    private static final Color[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.MAGENTA,
            Color.CYAN,
            Color.ORANGE,
            Color.PINK,
            Color.WHITE,
            Color.GRAY
    };

    private GameParams params = new GameParams(DEFAULT_ROW_COUNT, DEFAULT_COL_COUNT, DEFAULT_COLOR_COUNT);
    private Game game = new Game();

    /* Демонстрация работы с таймером (удалить, если не нужно в вашей игре) */
    private int time = 0;
    private Timer timer = new Timer(1000, e -> {
        time++;
        if ((attempt < 22) && !(GameOver)) {
            this.labelStatus.setText("Прошло времени (секунд): " + time + " Количество оставшихся попыток: " + attempt + "/22");
        }
    });


    private ParamsDialog dialogParams;


    public MainForm() {
        this.setTitle("Перекрасить");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        setJMenuBar(createMenuBar());
        this.pack();

        SwingUtils.setShowMessageDefaultErrorHandler();

        tableGameField.setRowHeight(DEFAULT_CELL_SIZE);
        JTableUtils.initJTableForArray(tableGameField, DEFAULT_CELL_SIZE, false, false, false, false);
        tableGameField.setIntercellSpacing(new Dimension(0, 0));
        tableGameField.setEnabled(false);

        tableGameField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final class DrawComponent extends Component {
                private int row = 0, column = 0;

                @Override
                public void paint(Graphics gr) {
                    Graphics2D g2d = (Graphics2D) gr;
                    int width = getWidth() - 2;
                    int height = getHeight() - 2;
                    paintCell(row, column, g2d, width, height);
                }
            }

            DrawComponent comp = new DrawComponent();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                comp.row = row;
                comp.column = column;
                return comp;
            }
        });

        newGame();

        updateWindowSize();
        updateView();

        dialogParams = new ParamsDialog(params, tableGameField, e -> newGame());

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if ((e.getID() == KeyEvent.KEY_PRESSED) && !(GameOver)) {
                    if (e.getKeyChar() == '1') {
                        game.solution(1);
                        attempt++;
                        updateView();
                    }
                    if (e.getKeyChar() == '2') {
                        game.solution(2);
                        attempt++;
                        updateView();
                    }
                    if (e.getKeyChar() == '3') {
                        game.solution(3);
                        attempt++;
                        updateView();
                    }
                    if (e.getKeyChar() == '4') {
                        game.solution(4);
                        attempt++;
                        updateView();
                    }
                    if (e.getKeyChar() == '5') {
                        game.solution(5);
                        attempt++;
                        updateView();
                    }
                    if (e.getKeyChar() == '6') {
                        game.solution(6);
                        attempt++;
                        updateView();
                    }
                    finaltime = time;
                }
                if ((game.exam()) && (attempt <= 22)) {
                    labelStatus.setText("Затрачено времени (секунд): " + finaltime + " Вы выиграли! " + attempt + "/22");
                    GameOver = true;
                } else if ((attempt == 22) && (!game.exam())) {
                    labelStatus.setText("Затрачено времени (секунд): " + finaltime + " Вы проиграли! " + attempt + "/22");
                    GameOver = true;
                }
                return false;
            }
        });
    }

    private JMenuItem createMenuItem(String text, String shortcut, Character mnemonic, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        if (shortcut != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut.replace('+', ' ')));
        }
        if (mnemonic != null) {
            menuItem.setMnemonic(mnemonic);
        }
        return menuItem;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBarMain = new JMenuBar();

        JMenu menuGame = new JMenu("Игра");
        menuBarMain.add(menuGame);
        menuGame.add(createMenuItem("Новая", "ctrl+N", null, e -> {
            newGame();
        }));
        menuGame.add(createMenuItem("Параметры", "ctrl+P", null, e -> {
            dialogParams.updateView();
            dialogParams.setVisible(true);
        }));
        menuGame.addSeparator();
        menuGame.add(createMenuItem("Выход", "ctrl+X", null, e -> {
            System.exit(0);
        }));

        JMenu menuView = new JMenu("Вид");
        menuBarMain.add(menuView);
        menuView.add(createMenuItem("Подогнать размер окна", null, null, e -> {
            updateWindowSize();
        }));
        menuView.addSeparator();
        SwingUtils.initLookAndFeelMenu(menuView);

        JMenu menuHelp = new JMenu("Справка");
        menuBarMain.add(menuHelp);
        menuHelp.add(createMenuItem("Правила", "ctrl+R", null, e -> {
            SwingUtils.showInfoMessageBox("Клавиши с 1 - 6 перекрашивают поле в соответсвующую цифру", "Правила");
        }));
        menuHelp.add(createMenuItem("О программе", "ctrl+A", null, e -> {
            SwingUtils.showInfoMessageBox(
                    "Игра 'Перекрасить'" +
                            "\n\nАвтор: Влад Мясо" +
                            "\ngithub: https://github.com/Klematisy",
                    "О программе"
            );
        }));

        return menuBarMain;
    }

    private void updateWindowSize() {
        int menuSize = this.getJMenuBar() != null ? this.getJMenuBar().getHeight() : 0;
        SwingUtils.setFixedSize(
                this,
                tableGameField.getWidth() + 2 * DEFAULT_GAP + 60,
                tableGameField.getHeight() + panelMain.getY() + labelStatus.getHeight() +
                        menuSize + 1 * DEFAULT_GAP + 2 * DEFAULT_GAP + 60
        );
        this.setMaximumSize(null);
        this.setMinimumSize(null);
    }

    private void updateView() {
        tableGameField.repaint();
    }


    private Font font = null;

    private Font getFont(int size) {
        if (font == null || font.getSize() != size) {
            font = new Font("Comic Sans MS", Font.BOLD, size);
        }
        return font;
    }

    private void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) {
        int cellValue = game.getColorNum(row, column); //matrix[row][column];

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cellValue <= 0) {
            return;
        }
        Color color = COLORS[cellValue - 1];

        int size = Math.min(cellWidth, cellHeight);
        int bound = (int) Math.round(size * 0.1);

        g2d.setColor(color);
        g2d.fillRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, bound * 3, bound * 3);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(bound, bound, size - 2 * bound, size - 2 * bound, bound * 3, bound * 3);

        g2d.setFont(getFont(size - 2 * bound));
        g2d.setColor(DrawUtils.getContrastColor(color));
        DrawUtils.drawStringInCenter(g2d, font, "" + cellValue, 0, 0, cellWidth, (int) Math.round(cellHeight * 0.95));
    }

    private void newGame() {
        game.newGame(params.getRowCount(), params.getColCount(), params.getColorCount());
        game.matrixRandomizer();
        JTableUtils.resizeJTable(tableGameField,
                params.getRowCount(), params.getColCount(),
                tableGameField.getRowHeight(), tableGameField.getRowHeight()
        );
        time = 0;
        attempt = 0;
        GameOver = false;
        timer.start();
        updateView();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, 10));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableGameField = new JTable();
        scrollPane1.setViewportView(tableGameField);
        labelStatus = new JLabel();
        labelStatus.setText("Label");
        panelMain.add(labelStatus, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
