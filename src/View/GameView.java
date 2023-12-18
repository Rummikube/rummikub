package View;

import Controller.GameController;
import Model.Player;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Controller.GameController.MAX_PLAYER_COUNT;
import static Controller.GameController.BOARD_HEIGHT;
import static Controller.GameController.BOARD_WIDTH;
import static Controller.GameController.HAND_HEIGHT;
import static Controller.GameController.HAND_WIDTH;

public class GameView {

    private JFrame frame;
    private JTextField nameTF, addressTF;
    private JButton makeRoomButton, connectButton, QuitButton, RunSortButton, GroupSortButton, DrawTileButton, readyButton, startButton;
    private JLabel loginErrorLabel, roomNoticePanel;
    private JPanel[][] board = new JPanel[BOARD_HEIGHT][BOARD_WIDTH]; // 보드의 타일들

    private JPanel[][] hand = new JPanel[HAND_HEIGHT][HAND_WIDTH];
    private JPanel[] playerIcon = new JPanel[MAX_PLAYER_COUNT];
    private JPanel[] roomReadyPanel = new JPanel[MAX_PLAYER_COUNT];
    private JLabel[] roomNameLabel = new JLabel[MAX_PLAYER_COUNT];

    private JPanel[] roomPlayerPanel = new JPanel[MAX_PLAYER_COUNT];

    private GameController gameController;

    public void updateRoomReadyPanel(Player.ReadyState readyState, int index){
        if(readyState == Player.ReadyState.READY){
            roomReadyPanel[index].setBackground(Color.green);
        }
        else{
            roomReadyPanel[index].setBackground(Color.red);
        }
    }

    public void updateRoomNameLabel(String name, int index){
        roomNameLabel[index].setText(name);
    }

    public JPanel[] getRoomReadyPanel() {
        return roomReadyPanel;
    }

    public void setRoomReadyPanel(JPanel[] roomReadyPanel) {
        this.roomReadyPanel = roomReadyPanel;
    }

    public JLabel[] getRoomNameLabel() {
        return roomNameLabel;
    }

    public void setRoomNameLabel(JLabel[] roomNameLabel) {
        this.roomNameLabel = roomNameLabel;
    }

    public JPanel[] getRoomPlayerPanel() {
        return roomPlayerPanel;
    }

    public void setRoomPlayerPanel(JPanel[] roomPlayerPanel) {
        this.roomPlayerPanel = roomPlayerPanel;
    }

    public void updateNameLabel(String name, int index){
        roomNameLabel[index].setText(name);
    }

    public void updatePlayers(Player[] players){
            for(int i = 0 ; i < MAX_PLAYER_COUNT ; i ++){
            if(players[i] != null){
                if(i == 0){
                    roomReadyPanel[i].setBackground(Color.cyan);
                }
                else updateRoomReadyPanel(players[i].getReadyState(), i);
                updateNameLabel(players[i].getName(), i);
                roomPlayerPanel[i].setVisible(true);
            }
            else{
                roomPlayerPanel[i].setVisible(false);
            }
        }

    }

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

    public JLabel getLoginErrorLabel() {
        return loginErrorLabel;
    }

    /**
     * Launch the application.
     */


    // 패널 전환 함수 LoginPanel, RoomPanel, GamePanel
    public void changePanel(String panelName){
        CardLayout cardLayout = (CardLayout)frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), panelName);
    }


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
    public GameView() {
        initialize();
    }

    // 유효한 IP주소인지 검사
    public static boolean isValidIP(String ipAddress) {
        Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(0, 0, 1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout(0, 0));


        // 로그인 패널 구현 부분
        JPanel LoginPanel = new JPanel();
        frame.getContentPane().add(LoginPanel, "LoginPanel");
        LoginPanel.setLayout(null);

        // ADD BUGIKUB LOGO
        ImageIcon bugi=new ImageIcon(new ImageIcon("asset/images/Bugikub.png").getImage());
        JLabel bugiLogo=new JLabel();
        bugiLogo.setIcon(bugi);


        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 1584, 861);

        //BackGroundColor
        panel.setBackground(new Color(223, 241, 239));

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
                // valid test
                if(nameTF.getText().isEmpty() || nameTF.getText().isBlank()){
                    loginErrorLabel.setText("이름을 입력해주세요.");
                    return;
                }
                gameController.makeRoom();
            }
        });

        panel.add(makeRoomButton);

        JPanel LoginImagePanel = new JPanel();
        LoginImagePanel.add(bugiLogo);
        LoginImagePanel.setBackground(new Color(223, 241,239));
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

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTF.getText();
                String address = addressTF.getText();

                if(name.isEmpty() || name.isBlank()){
                    loginErrorLabel.setText("이름을 입력해주세요.");
                    return;
                }
                else if(address.isEmpty() || address.isBlank() || !isValidIP(address)){
                    loginErrorLabel.setText("올바른 IP주소를 입력해주세요.");
                    return;
                }
                System.out.println("IP주소 : " + addressTF.getText() + "로 연결을 시도합니다.");
                gameController.connectRoom(addressTF.getText());
            }
        });

        // 오류 표시 라벨

        loginErrorLabel = new JLabel("");
        loginErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sl_panel.putConstraint(SpringLayout.NORTH, loginErrorLabel, 37, SpringLayout.SOUTH, makeRoomButton);
        sl_panel.putConstraint(SpringLayout.WEST, loginErrorLabel, 300, SpringLayout.WEST, panel);
        sl_panel.putConstraint(SpringLayout.SOUTH, loginErrorLabel, -80, SpringLayout.SOUTH, panel);
        sl_panel.putConstraint(SpringLayout.EAST, loginErrorLabel, -300, SpringLayout.EAST, panel);
        panel.add(loginErrorLabel);

        // 게임 패널 레이아웃 설정
        JPanel GamePanel = new JPanel();
        frame.getContentPane().add(GamePanel, "GamePanel");
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

        // 플레이어 방
        JPanel RoomPanel = new JPanel();
        frame.getContentPane().add(RoomPanel, "RoomPanel");
        RoomPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel_11 = new JPanel();
        panel_11.setPreferredSize(new Dimension(10, 100));
        RoomPanel.add(panel_11, BorderLayout.NORTH);
        panel_11.setLayout(new BorderLayout(0, 0));

        JPanel panel_22 = new JPanel();
        panel_11.add(panel_22, BorderLayout.CENTER);
        panel_22.setLayout(null);

        roomNoticePanel = new JLabel("New label");
        roomNoticePanel.setHorizontalAlignment(SwingConstants.CENTER);
        roomNoticePanel.setBounds(160, 33, 1264, 35);
        panel_22.add(roomNoticePanel);

        JPanel panel_12 = new JPanel();
        panel_12.setPreferredSize(new Dimension(300, 10));
        RoomPanel.add(panel_12, BorderLayout.EAST);
        panel_12.setLayout(new BoxLayout(panel_12, BoxLayout.Y_AXIS));

        JPanel panel_19 = new JPanel();
        panel_12.add(panel_19);
        panel_19.setLayout(new BorderLayout(0, 0));

        JPanel panel_21 = new JPanel();
        panel_21.setPreferredSize(new Dimension(10, 30));
        panel_19.add(panel_21, BorderLayout.NORTH);

        JPanel panel_27 = new JPanel();
        panel_27.setPreferredSize(new Dimension(30, 10));
        panel_19.add(panel_27, BorderLayout.WEST);

        JPanel panel_28 = new JPanel();
        panel_28.setPreferredSize(new Dimension(30, 10));
        panel_19.add(panel_28, BorderLayout.EAST);

        JPanel panel_31 = new JPanel();
        panel_31.setPreferredSize(new Dimension(10, 15));
        panel_19.add(panel_31, BorderLayout.SOUTH);

        startButton = new JButton("New button");
        panel_19.add(startButton, BorderLayout.CENTER);

        JPanel panel_20 = new JPanel();
        panel_12.add(panel_20);
        panel_20.setLayout(new BorderLayout(0, 0));

        JPanel panel_24 = new JPanel();
        panel_24.setPreferredSize(new Dimension(10, 30));
        panel_20.add(panel_24, BorderLayout.SOUTH);

        JPanel panel_29 = new JPanel();
        panel_29.setPreferredSize(new Dimension(30, 10));
        panel_20.add(panel_29, BorderLayout.WEST);

        JPanel panel_30 = new JPanel();
        panel_30.setPreferredSize(new Dimension(30, 10));
        panel_20.add(panel_30, BorderLayout.EAST);

        JPanel panel_32 = new JPanel();
        panel_32.setPreferredSize(new Dimension(10, 15));
        panel_20.add(panel_32, BorderLayout.NORTH);

        readyButton = new JButton("New button");
        panel_20.add(readyButton, BorderLayout.CENTER);

        JPanel panel_13 = new JPanel();
        RoomPanel.add(panel_13, BorderLayout.CENTER);
        panel_13.setLayout(new BorderLayout(0, 0));

        JPanel panel_14 = new JPanel();
        panel_14.setPreferredSize(new Dimension(50, 10));
        panel_13.add(panel_14, BorderLayout.WEST);

        JPanel panel_15 = new JPanel();
        panel_15.setPreferredSize(new Dimension(50, 10));
        panel_13.add(panel_15, BorderLayout.EAST);

        JPanel panel_16 = new JPanel();
        panel_16.setPreferredSize(new Dimension(10, 50));
        panel_13.add(panel_16, BorderLayout.NORTH);

        JPanel panel_17 = new JPanel();
        panel_17.setPreferredSize(new Dimension(10, 50));
        panel_13.add(panel_17, BorderLayout.SOUTH);

        JPanel panel_18 = new JPanel();
        panel_13.add(panel_18, BorderLayout.CENTER);
        panel_18.setLayout(new GridLayout(2, 2, 50, 20));


        for(int i = 0 ; i < 2 ; i ++) {
            JPanel roomRowPanel = new JPanel();
            FlowLayout fl_playerRoomRow1 = (FlowLayout) roomRowPanel.getLayout();
            fl_playerRoomRow1.setHgap(30);
            panel_18.add(roomRowPanel);
//            roomRowPanel.setBackground(Color.black);

            for(int j = 0 ; j < 2 ; j ++) {
                JPanel RoomPlayerPanel = new JPanel();
                roomPlayerPanel[i * 2 + j] = RoomPlayerPanel;
//                RoomPlayerPanel.setBackground(Color.cyan);
                JPanel tmpPanel = new JPanel();
                tmpPanel.setPreferredSize(new Dimension(550, 310));
                RoomPlayerPanel.setPreferredSize(new Dimension(550, 310));
                roomRowPanel.add(tmpPanel);
                tmpPanel.add(RoomPlayerPanel, BorderLayout.CENTER);
                RoomPlayerPanel.setLayout(new BorderLayout(0, 0));

                JPanel readyPanel = new JPanel();
                roomReadyPanel[i * 2 + j] = readyPanel;
                readyPanel.setPreferredSize(new Dimension(10, 50));
                RoomPlayerPanel.add(readyPanel, BorderLayout.SOUTH);
//                readyPanel.setBackground(Color.red);

                JPanel tmp1 = new JPanel();
                RoomPlayerPanel.add(tmp1, BorderLayout.CENTER);
                tmp1.setLayout(new BorderLayout(0, 0));

                JPanel tmp2 = new JPanel();
                tmp2.setPreferredSize(new Dimension(110, 10));
                tmp1.add(tmp2, BorderLayout.WEST);

                JPanel tmp3 = new JPanel();
                tmp3.setPreferredSize(new Dimension(110, 10));
                tmp1.add(tmp3, BorderLayout.EAST);

                JLabel playerImageLabel = new JLabel("player_Image");
                playerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                tmp1.add(playerImageLabel, BorderLayout.CENTER);

                JLabel playerNameLabel = new JLabel("player_name");
                roomNameLabel[i * 2 + j] = playerNameLabel;
                playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                playerNameLabel.setPreferredSize(new Dimension(57, 30));
                RoomPlayerPanel.add(playerNameLabel, BorderLayout.NORTH);

            }
        }

    }
}