package View;

import Controller.GameController;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Controller.GameController.MAX_PLAYER_COUNT;
import static Controller.GameController.BOARD_HEIGHT;
import static Controller.GameController.BOARD_WIDTH;
import static Controller.GameController.HAND_HEIGHT;
import static Controller.GameController.HAND_WIDTH;

public class GameView {

    private JFrame frame;
    private JTextField nameTF, addressTF;
    private JButton makeRoomButton, connectButton, QuitButton, RunSortButton, GroupSortButton, DrawTileButton;
    private JLabel errorLabel;
    private JPanel[][] board = new JPanel[BOARD_HEIGHT][BOARD_WIDTH]; // 보드의 타일들

    private JPanel[][] hand = new JPanel[HAND_HEIGHT][HAND_WIDTH];
    private JPanel[] playerIcon = new JPanel[MAX_PLAYER_COUNT];

    private GameController gameController;


    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getNameTF() {
        return nameTF;
    }

    public JTextField getAddressTF() {
        return addressTF;
    }

    public JButton getMakeRoomButton() {
        return makeRoomButton;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JButton getQuitButton() {
        return QuitButton;
    }

    public JButton getRunSortButton() {
        return RunSortButton;
    }

    public JButton getGroupSortButton() {
        return GroupSortButton;
    }

    public JButton getDrawTileButton() {
        return DrawTileButton;
    }

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    /**
     * Launch the application.
     */

    // 타일 패널 이동 함수
    public void moveTileUI(boolean startHand, boolean toHand, int startRow, int startCol, int endRow, int endCol) {
        // 손패에 있는 타일일 경우
        JPanel movePanel = startHand ? hand[startRow][startCol] : board[startRow][startCol];
        JPanel desPanel = toHand ? hand[endRow][endCol] : board[endRow][endCol];

        if (startHand) hand[startRow][startCol] = desPanel;
        else board[startRow][startCol] = desPanel;

        if (toHand) hand[endRow][endCol] = movePanel;
        else board[endRow][endCol] = movePanel;
    }

    public void startUI() {
        frame.setVisible(true);
    }

    /**
     * Create the application.
     */
    public GameView() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(0, 0, 1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout(0, 0));

        // 로그인 패널 구현 부분
        JPanel LoginPanel = new JPanel();
        frame.getContentPane().add(LoginPanel, "name_1157230094774500");
        LoginPanel.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1584, 861);
        LoginPanel.add(panel);
        SpringLayout sl_panel = new SpringLayout();
        panel.setLayout(sl_panel);

        JLabel addressLabel = new JLabel("IP 주소");
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(addressLabel);

        addressTF = new JTextField();
        sl_panel.putConstraint(SpringLayout.SOUTH, addressTF, -300, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.NORTH, addressLabel, 8, SpringLayout.NORTH, addressTF);
        sl_panel.putConstraint(SpringLayout.SOUTH, addressLabel, 0, SpringLayout.SOUTH, addressTF);
        panel.add(addressTF);
        addressTF.setColumns(10);

        nameTF = new JTextField();
        sl_panel.putConstraint(SpringLayout.NORTH, addressTF, 60, SpringLayout.SOUTH, nameTF);
        sl_panel.putConstraint(SpringLayout.NORTH, nameTF, 425, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, nameTF, -400, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, addressTF, 0, SpringLayout.WEST, nameTF);
        sl_panel.putConstraint(SpringLayout.EAST, addressTF, 0, SpringLayout.EAST, nameTF);
        sl_panel.putConstraint(SpringLayout.WEST, nameTF, 500, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.EAST, nameTF, -500, SpringLayout.EAST, panel);
        nameTF.setToolTipText("이름을 입력하세요.");
        panel.add(nameTF);
        nameTF.setColumns(10);

        JLabel nameLabel = new JLabel("이름");
        sl_panel.putConstraint(SpringLayout.WEST, nameLabel, 350, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.WEST, addressLabel, 0, SpringLayout.WEST, nameLabel);
        sl_panel.putConstraint(SpringLayout.EAST, addressLabel, 0, SpringLayout.EAST, nameLabel);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sl_panel.putConstraint(SpringLayout.NORTH, nameLabel, 0, SpringLayout.NORTH, nameTF);
        sl_panel.putConstraint(SpringLayout.SOUTH, nameLabel, 0, SpringLayout.SOUTH, nameTF);
        sl_panel.putConstraint(SpringLayout.EAST, nameLabel, -50, SpringLayout.WEST, nameTF);
        panel.add(nameLabel);


        makeRoomButton = new JButton("방 만들기");
        sl_panel.putConstraint(SpringLayout.NORTH, makeRoomButton, 73, SpringLayout.SOUTH, addressTF);
        sl_panel.putConstraint(SpringLayout.WEST, makeRoomButton, 400, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, makeRoomButton, 704, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, makeRoomButton, -850, SpringLayout.EAST, panel);
        makeRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(gameController);
                gameController.makeRoom();
            }
        });

        panel.add(makeRoomButton);

        JPanel LoginImagePanel = new JPanel();
        sl_panel.putConstraint(SpringLayout.NORTH, LoginImagePanel, 60, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.WEST, LoginImagePanel, 300, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, LoginImagePanel, 320, SpringLayout.NORTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, LoginImagePanel, -300, SpringLayout.EAST, panel);
        panel.add(LoginImagePanel);


        connectButton = new JButton("참여하기");
        sl_panel.putConstraint(SpringLayout.NORTH, connectButton, 73, SpringLayout.SOUTH, addressTF);
        sl_panel.putConstraint(SpringLayout.WEST, connectButton, 865, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, connectButton, -157, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, connectButton, -385, SpringLayout.EAST, panel);
        panel.add(connectButton);

        // 오류 표시 라벨

        errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sl_panel.putConstraint(SpringLayout.NORTH, errorLabel, 37, SpringLayout.SOUTH, makeRoomButton);
        sl_panel.putConstraint(SpringLayout.WEST, errorLabel, 300, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, errorLabel, -80, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, errorLabel, -300, SpringLayout.EAST, panel);
        panel.add(errorLabel);

        // 게 패널 레이아웃 설정
        JPanel GamePanel = new JPanel();
        frame.getContentPane().add(GamePanel, "name_1157230101844400");
        GamePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
        GamePanel.setLayout(new BoxLayout(GamePanel, BoxLayout.X_AXIS));


        JPanel PlayerIconPanel = new JPanel();
        GamePanel.add(PlayerIconPanel);
        PlayerIconPanel.setLayout(new BoxLayout(PlayerIconPanel, BoxLayout.Y_AXIS));


        // 플레이어 아이콘 패널 추가
        for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
            JPanel playerIconBorder = new JPanel();
            PlayerIconPanel.add(playerIconBorder);
            playerIconBorder.setLayout(new BorderLayout(0, 0));

            JPanel playerIconWest = new JPanel();
            playerIconWest.setPreferredSize(new Dimension(50, 10));
            playerIconBorder.add(playerIconWest, BorderLayout.WEST);

            JPanel playerIconSouth = new JPanel();
            playerIconSouth.setPreferredSize(new Dimension(10, 50));
            playerIconBorder.add(playerIconSouth, BorderLayout.SOUTH);

            JPanel playerIconNorth = new JPanel();
            playerIconNorth.setPreferredSize(new Dimension(10, 50));
            playerIconBorder.add(playerIconNorth, BorderLayout.NORTH);

            JPanel playerIconPanel = new JPanel();
            playerIconBorder.add(playerIconPanel, BorderLayout.CENTER);

            playerIcon[i] = playerIconPanel;

            playerIcon[i].setBackground(Color.black);
        }


        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new BorderLayout(0, 0));
        GamePanel.add(panel_3);

        JPanel panel_1 = new JPanel();
        panel_3.add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension(150, 1));
        panel_1.add(westPanel, BorderLayout.WEST);

        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(150, 1));
        panel_1.add(eastPanel, BorderLayout.EAST);

        JPanel HandLayoutPanel = new JPanel();
        HandLayoutPanel.setPreferredSize(new Dimension(1, 250));
        panel_1.add(HandLayoutPanel, BorderLayout.CENTER);
        HandLayoutPanel.setLayout(new BorderLayout(0, 0));

        JPanel HandWestPanel = new JPanel();
        HandWestPanel.setPreferredSize(new Dimension(20, 1));
        HandLayoutPanel.add(HandWestPanel, BorderLayout.WEST);

        JPanel HandEastPanel = new JPanel();
        HandEastPanel.setPreferredSize(new Dimension(20, 1));
        HandLayoutPanel.add(HandEastPanel, BorderLayout.EAST);

        JPanel HandPanel = new JPanel();
        HandLayoutPanel.add(HandPanel, BorderLayout.CENTER);
        HandPanel.setLayout(new GridLayout(2, 10, 0, 0));

        JPanel panell = new JPanel();
        panell.setPreferredSize(new Dimension(10, 20));
        panel_1.add(panell, BorderLayout.SOUTH);

        JPanel panel_5 = new JPanel();
        panel_3.add(panel_5, BorderLayout.CENTER);
        panel_5.setLayout(new BorderLayout(0, 0));

        JPanel panel_6 = new JPanel();
        panel_6.setPreferredSize(new Dimension(1, 20));
        panel_5.add(panel_6, BorderLayout.NORTH);

        JPanel panel_7 = new JPanel();
        panel_5.add(panel_7, BorderLayout.CENTER);
        panel_7.setLayout(new BorderLayout(0, 0));

        JPanel panel_8 = new JPanel();
        panel_8.setPreferredSize(new Dimension(15, 10));
        panel_7.add(panel_8, BorderLayout.WEST);

        JPanel panel_9 = new JPanel();
        panel_9.setPreferredSize(new Dimension(15, 10));
        panel_7.add(panel_9, BorderLayout.EAST);

        JPanel BoardPanel = new JPanel();
        panel_7.add(BoardPanel, BorderLayout.CENTER);
        BoardPanel.setLayout(new GridLayout(BOARD_HEIGHT, BOARD_WIDTH, 0, 5));

        // board 배열에 패널 추가
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            JPanel curRowPanel = new JPanel();
            BoardPanel.add(curRowPanel);
            for (int j = 0; j < BOARD_WIDTH; j++) {
                JPanel tmp = new JPanel();
                board[i][j] = tmp;
                curRowPanel.add(tmp);
                board[i][j].setPreferredSize(new Dimension(50, 75));
                board[i][j].setBackground(Color.black);
            }
        }

        // hand 배열에 패널 추가
        for (int i = 0; i < HAND_HEIGHT; i++) {
            JPanel curRowPanel = new JPanel();
            HandPanel.add(curRowPanel);
            for (int j = 0; j < HAND_WIDTH; j++) {
                JPanel tmp = new JPanel();
                hand[i][j] = tmp;
                curRowPanel.add(tmp);
                hand[i][j].setPreferredSize(new Dimension(80, 120));
                hand[i][j].setBackground(Color.black);
            }
        }


        // 오른쪽 패널
        JPanel MenuPanel = new JPanel();
        GamePanel.add(MenuPanel);
        MenuPanel.setLayout(new BoxLayout(MenuPanel, BoxLayout.Y_AXIS));

        JPanel panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(150, 50));
        MenuPanel.add(panel_2);
        panel_2.setLayout(null);


        // 종료 버튼
        QuitButton = new JButton("New button");
        QuitButton.setBounds(25, 43, 100, 100);
        panel_2.add(QuitButton);

        JPanel panel_4 = new JPanel();
        panel_4.setPreferredSize(new Dimension(100, 200));
        MenuPanel.add(panel_4);
        panel_4.setLayout(null);


        // Run 정렬 버튼
        RunSortButton = new JButton("New button");
        RunSortButton.setBounds(12, 45, 126, 123);
        panel_4.add(RunSortButton);


        // Group 정렬 버튼
        GroupSortButton = new JButton("New button");
        GroupSortButton.setBounds(12, 178, 126, 107);
        panel_4.add(GroupSortButton);

        JPanel panel_10 = new JPanel();
        panel_10.setPreferredSize(new Dimension(100, 200));
        MenuPanel.add(panel_10);
        panel_10.setLayout(null);

        // 타일 드로우 버튼
        DrawTileButton = new JButton("New button");
        DrawTileButton.setBounds(12, 10, 126, 316);
        panel_10.add(DrawTileButton);


    }
}