package View;

import Controller.GameController;
import Model.Board;
import Model.Player;
import Model.Tile;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Controller.GameController.MAX_PLAYER_COUNT;
import static Controller.GameController.BOARD_HEIGHT;
import static Controller.GameController.BOARD_WIDTH;
import static Controller.GameController.HAND_HEIGHT;
import static Controller.GameController.HAND_WIDTH;

class TileComponent extends JLabel{
    private int row, col;
    private boolean inBoard;
    public TileComponent(int row, int col, boolean inBoard) {
        this.row = row;
        this.col = col;
        this.inBoard = inBoard;
    }

    public boolean isInBoard() {
        return inBoard;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setInBoard(boolean inBoard) {
        this.inBoard = inBoard;
    }

    @Override
    public String toString() {
        return "TileComponent{" +
                "row=" + row +
                ", col=" + col +
                ", inBoard=" + inBoard +
                '}';
    }

}




public class GameView {
    private JFrame frame;
    private JTextField nameTF, addressTF;
    private JButton makeRoomButton, connectButton, QuitButton, RunSortButton, GroupSortButton, DrawTileButton, readyButton, startButton;
    private JLabel loginErrorLabel, roomNoticePanel;

    static ImageIcon tileIcon = new ImageIcon(new ImageIcon("asset/images/tile.png").getImage());
    private TileComponent curClickedPanel;
    private boolean isDragging = false;
    private Point originalPoint;

    int offsetX = 0;
    int offsetY = 0;


    private TileComponent[][] board = new TileComponent[BOARD_HEIGHT][BOARD_WIDTH]; // 보드의 타일들

    private TileComponent[][] hand = new TileComponent[HAND_HEIGHT][HAND_WIDTH];
    private JPanel[] playerIcon = new JPanel[MAX_PLAYER_COUNT];
    private JPanel[] roomReadyPanel = new JPanel[MAX_PLAYER_COUNT];
    private JLabel[] roomNameLabel = new JLabel[MAX_PLAYER_COUNT];

    private JLabel[][] tileNumberLabel = new JLabel[HAND_HEIGHT][HAND_WIDTH];
    private JPanel[] roomPlayerPanel = new JPanel[MAX_PLAYER_COUNT];


    private Rectangle[][] handPoints = new Rectangle[HAND_HEIGHT][HAND_WIDTH];
    private Rectangle[][] boardPoints = new Rectangle[BOARD_HEIGHT][BOARD_WIDTH];
    private GameController gameController;

    private Player [] players;

    public void updateRoomReadyPanel(Player.ReadyState readyState, int index){
        if(readyState == Player.ReadyState.READY){
            roomReadyPanel[index].setBackground(Color.green);
        }
        else{
            roomReadyPanel[index].setBackground(Color.red);
        }
    }

    public void updateTile(String color, int num, int row, int col){
        Color fontColor;
        JLabel curLabel = tileNumberLabel[row][col];
        if(color.equals("Red")){
            fontColor = Color.red;
        }
        else if(color.equals("Orange")){
            fontColor = Color.orange;
        }
        else if(color.equals("Blue")){
            fontColor = Color.blue;
        }
        else if(color.equals("Black")){
            fontColor = Color.BLACK;
        }
        else{
            //TODO 부기 이미지로 변경
            curLabel.setForeground(Color.cyan);
            curLabel.setText("14");
            return;
        }
        curLabel.setForeground(fontColor);
        curLabel.setText(String.valueOf(num));
    }

    public void setInvisibleTile(int row, int col, boolean isHand){
        if(isHand){
            hand[row][col].setVisible(false);
        }
        else{
            board[row][col].setVisible(false);
        }
    }

    public void setTransParentPanel(int row, int col, boolean isHand){
        JLabel[][] curPanel;
        if(isHand){
            curPanel = hand;
        }
        else{
            curPanel = board;
        }
        curPanel[row][col].setOpaque(false);
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
                    //JLabel tileCountLabel = new JLabel("Tiles: " + players[i].getTiles());
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
        System.out.println(panelName + "패널로 변경합니다.");
        CardLayout cardLayout = (CardLayout)frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), panelName);
    }


    public void changeTileComponent(TileComponent a, TileComponent b, boolean isHandToBoard){
        int sr = a.getRow();
        int sc = a.getCol();
        int er = b.getRow();
        int ec = b.getCol();
        b.setCol(sc);
        b.setRow(sr);

        a.setCol(ec);
        a.setRow(er);

        if(isHandToBoard){
            a.setInBoard(true);
            b.setInBoard(false);
        }

    }

    // 타일 패널 이동 함수
    public boolean moveTileUI(TileComponent start, TileComponent end) {
        // 손패에 있는 타일일 경우
        TileComponent startPanel = start.isInBoard() ? board[start.getRow()][start.getCol()] : hand[start.getRow()][start.getCol()];
        TileComponent endPanel = end.isInBoard() ? board[end.getRow()][end.getCol()] : hand[end.getRow()][end.getCol()];

        System.out.println(startPanel + " " + endPanel);


        if (!startPanel.isInBoard()){
            // hand -> hand
            if(!endPanel.isInBoard()){
                hand[start.getRow()][start.getCol()] = endPanel;
                endPanel.setBounds(handPoints[start.getRow()][start.getCol()]);
                hand[end.getRow()][end.getCol()] = startPanel;
                startPanel.setBounds(handPoints[end.getRow()][end.getCol()]);
                changeTileComponent(startPanel, endPanel, false);
                return true;
            }
            // hand -> board
            else{
                if(!gameController.isInMyTurn()) return false;
                if(gameController.canAdd(endPanel.getRow(), endPanel.getCol())){
                    hand[start.getRow()][start.getCol()] = endPanel;
                    endPanel.setBounds(handPoints[start.getRow()][start.getCol()]);
                    board[end.getRow()][end.getCol()] = startPanel;
                    startPanel.setBounds(boardPoints[end.getRow()][end.getCol()]);
                    changeTileComponent(startPanel, endPanel, true);
                    return true;
                }
                else{
                    return false;
                }
            }
        }

        else{
            if(!gameController.isInMyTurn()) return false;
            if(endPanel.isInBoard()){
                board[start.getRow()][start.getCol()] = endPanel;
                endPanel.setBounds(boardPoints[start.getRow()][start.getCol()]);
                board[end.getRow()][end.getCol()] = startPanel;
                startPanel.setBounds(boardPoints[end.getRow()][end.getCol()]);
                changeTileComponent(startPanel, endPanel, false);
                return true;
            }
            else return false;
        }
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

        JPanel LoginImagePanel = new JPanel(/*new GridLayout(1,1,5,5)*/);
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
            playerIconWest.setBackground(new Color(223, 241, 239));
            playerIconWest.setPreferredSize(new Dimension(50, 10));
            playerIconBorder.add(playerIconWest, BorderLayout.WEST);

            // "player" + (i+1) Label add
            JPanel playerIconSouth = new JPanel();
            playerIconSouth.setLayout(new BorderLayout());
            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.setBackground(new Color(223, 241, 239));
            centerPanel.add(Box.createRigidArea(new Dimension(40, 0)));
            JLabel playerName = new JLabel("Player " + (i + 1));
            JLabel remainTiles = new JLabel("n tiles remain"); // 남아있는 타일 개수 출력
            JPanel playerNamePanel = new JPanel();
            playerNamePanel.setBackground(new Color(223, 241, 239));
            playerNamePanel.add(playerName);
            playerNamePanel.add(remainTiles);
            centerPanel.add(playerNamePanel);
            playerIconSouth.add(centerPanel, BorderLayout.CENTER);
            playerIconBorder.add(playerIconSouth, BorderLayout.SOUTH);


            JPanel playerIconNorth = new JPanel();
            playerIconNorth.setBackground(new Color(223, 241, 239));
            playerIconNorth.setPreferredSize(new Dimension(10, 50));
            playerIconBorder.add(playerIconNorth, BorderLayout.NORTH);

            JPanel playerIconPanel = new JPanel();
            playerIconPanel.setBackground(new Color(223, 241, 239));
            playerIconBorder.add(playerIconPanel, BorderLayout.CENTER);

            playerIcon[i] = playerIconPanel;

            ImageIcon empProfileIcon=new ImageIcon(new ImageIcon("asset/images/emptyProfile.png").getImage());
            Image scaledImage = empProfileIcon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            empProfileIcon = new ImageIcon(scaledImage);
            JLabel emtPlayerIcon=new JLabel();
            emtPlayerIcon.setIcon(empProfileIcon);
            playerIcon[i].add(emtPlayerIcon);
        }


        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new BorderLayout(0, 0));
        GamePanel.add(panel_3);

        JPanel panel_5 = new JPanel();
        panel_3.add(panel_5, BorderLayout.CENTER);
        panel_5.setLayout(new BorderLayout(0, 0));

        JLayeredPane BoardPanel = new JLayeredPane();
        panel_5.add(BoardPanel, BorderLayout.CENTER);
        BoardPanel.setPreferredSize(new Dimension(1150, 10));
        BoardPanel.setBackground(new Color(223, 241, 239));


        int boardY = 20;
        // board 배열에 패널 추가
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            int boardX = 45;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                TileComponent tmp = new TileComponent(i, j, true);
                board[i][j] = tmp;
                BoardPanel.add(tmp);
                tmp.setIcon(tileIcon);
                Rectangle tPoint = new Rectangle(boardX, boardY, 50, 75);
                board[i][j].setBounds(tPoint);

                boardPoints[i][j] = tPoint;
                board[i][j].setBackground(Color.black);
                boardX += 55;

                tmp.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        curClickedPanel = tmp;
                        originalPoint = tmp.getLocation();
                        offsetX = e.getX();
                        offsetY = e.getY();
                        isDragging = true;
                        BoardPanel.setLayer(tmp, JLayeredPane.DRAG_LAYER);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        curClickedPanel.setLocation(originalPoint);
                        curClickedPanel = null;
                        originalPoint = null;
                        isDragging = false;
                        BoardPanel.setLayer(tmp, JLayeredPane.DEFAULT_LAYER);
                    }
                });
                tmp.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if(isDragging){
                            int newX = curClickedPanel.getX() + e.getX() - offsetX;
                            int newY = curClickedPanel.getY() + e.getY() - offsetY;
                            curClickedPanel.setLocation(newX, newY);
                        }
                    }
                });
            }
            boardY += 80;
        }


        int handY = 600;
        Font tileNumFont = new Font("Arial", Font.BOLD, 40);
        // hand 배열에 패널 추가
        for (int i = 0; i < HAND_HEIGHT; i++) {
            int handX = 170;
            for (int j = 0; j < HAND_WIDTH; j++) {
                TileComponent tmp = new TileComponent(i, j, false);
                hand[i][j] = tmp;
                BoardPanel.add(tmp);
                hand[i][j].setBackground(Color.yellow);
                hand[i][j].setLayout(null);
                Rectangle tPoint = new Rectangle(handX, handY, 80, 120);
                hand[i][j].setBounds(tPoint);
                handPoints[i][j] = tPoint;
                JLabel tmpLabel=new JLabel("");
                tmp.setIcon(tileIcon);
                tileNumberLabel[i][j] = tmpLabel;
                tmpLabel.setForeground(Color.cyan);
                tmpLabel.setFont(tileNumFont);
                tmpLabel.setHorizontalAlignment(SwingConstants.CENTER);
                tmpLabel.setBounds(10, 15, 60, 60);
                tmpLabel.setOpaque(false);
                hand[i][j].add(tmpLabel);
                handX += 85;

                tmp.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        curClickedPanel = tmp;
                        originalPoint = tmp.getLocation();
                        offsetX = e.getX();
                        offsetY = e.getY();
                        isDragging = true;
                        BoardPanel.setLayer(tmp, JLayeredPane.DRAG_LAYER);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                        List<Component> foundComponents = LayeredPaneUtils.getAllComponentsAt(BoardPanel, e.getLocationOnScreen());
                        boolean flag = false;
                        System.out.println(foundComponents.size());

                        for(Component comp : foundComponents) {
                            System.out.println(comp);
                            if (comp != tmp && comp instanceof TileComponent) {
                                System.out.println(comp);
                                TileComponent tcomp = (TileComponent) comp;
                                if(moveTileUI(tmp, tcomp)){
                                    gameController.changeTiles(tmp.isInBoard(), tcomp.isInBoard(), tmp.getRow(), tmp.getCol(), tcomp.getRow(), tcomp.getCol());
                                }
                                else{
                                    curClickedPanel.setLocation(originalPoint);
                                }
                                break;
                            }
                        }
                        if(!flag) curClickedPanel.setLocation(originalPoint);

                        curClickedPanel = null;
                        originalPoint = null;
                        isDragging = false;
                        BoardPanel.setLayer(tmp, JLayeredPane.DEFAULT_LAYER);
                    }
                });
                tmp.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if(isDragging){
                            int newX = curClickedPanel.getX() + e.getX() - offsetX;
                            int newY = curClickedPanel.getY() + e.getY() - offsetY;
                            curClickedPanel.setLocation(newX, newY);
                        }
                    }
                });
            }
            handY += 125;
        }


        // 오른쪽 패널
        JPanel MenuPanel = new JPanel();
        GamePanel.add(MenuPanel);
        MenuPanel.setLayout(new BoxLayout(MenuPanel, BoxLayout.Y_AXIS));

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(223, 241, 239));
        panel_2.setPreferredSize(new Dimension(150, 50));
        MenuPanel.add(panel_2);
        panel_2.setLayout(null);

        // 종료 버튼
        ImageIcon quitBtnIcon = new ImageIcon(new ImageIcon("asset/images/QuitBTN.png").getImage());
        QuitButton = new JButton();
        QuitButton.setIcon(quitBtnIcon);
        QuitButton.setBackground(new Color(223, 241, 239));
        QuitButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        QuitButton.setContentAreaFilled(false);
        QuitButton.setFocusPainted(false);
        QuitButton.setOpaque(false);
        QuitButton.setBounds(25, 43, 100, 100);
        panel_2.add(QuitButton);


        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(223, 241, 239));
        panel_4.setPreferredSize(new Dimension(100, 200));
        MenuPanel.add(panel_4);
        panel_4.setLayout(null);


        // Run 정렬 버튼
        ImageIcon runIcon=new ImageIcon(new ImageIcon("asset/images/RunSortBTN.png").getImage());
        RunSortButton = new JButton();
        RunSortButton.setIcon(runIcon);
        RunSortButton.setBackground(new Color(223, 241, 239));
        RunSortButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        RunSortButton.setContentAreaFilled(false);
        RunSortButton.setFocusPainted(false);
        RunSortButton.setOpaque(false);
        RunSortButton.setBounds(12, 45, 126, 123);
        RunSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.sortByRun();
            }
        });
        panel_4.add(RunSortButton);


        // Group 정렬 버튼
        ImageIcon groupIcon=new ImageIcon(new ImageIcon("asset/images/GroupSortBTN.png").getImage());
        GroupSortButton = new JButton();
        GroupSortButton.setIcon(groupIcon);
        GroupSortButton.setBackground(new Color(223, 241, 239));
        GroupSortButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        GroupSortButton.setBounds(12, 178, 126, 113);
        GroupSortButton.setContentAreaFilled(false);
        GroupSortButton.setFocusPainted(false);
        GroupSortButton.setOpaque(false);
        GroupSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.sortByGroup();
            }
        });
        panel_4.add(GroupSortButton);

        JPanel panel_10 = new JPanel();
        panel_10.setBackground(new Color(223, 241, 239));
        panel_10.setPreferredSize(new Dimension(100, 200));
        MenuPanel.add(panel_10);
        panel_10.setLayout(null);

        // 타일 드로우 버튼
        ImageIcon drawTile=new ImageIcon(new ImageIcon("asset/images/AddTilesBTN.png").getImage());
        DrawTileButton = new JButton();
        DrawTileButton.setIcon(drawTile);
        DrawTileButton.setBackground(new Color(223, 241, 239));
        DrawTileButton.setBorder(new EmptyBorder(0,0,0,0));
        DrawTileButton.setBounds(12, 10, 126, 316);
        DrawTileButton.setContentAreaFilled(false);
        DrawTileButton.setFocusPainted(false);
        DrawTileButton.setOpaque(false);
        panel_10.add(DrawTileButton);

        // 플레이어 방
        JPanel RoomPanel = new JPanel();
        RoomPanel.setBackground(new Color(223, 241, 239));
        frame.getContentPane().add(RoomPanel, "RoomPanel");
        RoomPanel.setLayout(new BorderLayout(0, 0));

        JPanel panel_11 = new JPanel();
        panel_11.setPreferredSize(new Dimension(10, 100));
        RoomPanel.add(panel_11, BorderLayout.NORTH);
        panel_11.setLayout(new BorderLayout(0, 0));

        JPanel panel_22 = new JPanel();
        panel_22.setBackground(new Color(223, 241, 239));
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
        panel_21.setBackground(new Color(223,241,239));
        panel_19.add(panel_21, BorderLayout.NORTH);

        JPanel panel_27 = new JPanel();
        panel_27.setPreferredSize(new Dimension(30, 10));
        panel_27.setBackground(new Color(223,241,239));
        panel_19.add(panel_27, BorderLayout.WEST);

        JPanel panel_28 = new JPanel();
        panel_28.setPreferredSize(new Dimension(30, 10));
        panel_28.setBackground(new Color(223,241,239));
        panel_19.add(panel_28, BorderLayout.EAST);

        JPanel panel_31 = new JPanel();
        panel_31.setPreferredSize(new Dimension(10, 15));
        panel_31.setBackground(new Color(223,241,239));
        panel_19.add(panel_31, BorderLayout.SOUTH);

        // start button
        ImageIcon start=new ImageIcon(new ImageIcon("asset/images/start.png").getImage());
        startButton = new JButton();
        startButton.setIcon(start);
        startButton.setBackground(new Color(223, 241, 239));
        startButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);


        // 시작 버튼 리스너
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(gameController.getIndex() == 0){
                    if(gameController.checkReady()){
                        gameController.serverStartGame();
                    }
                    else{
                        roomNoticePanel.setText("모든 사람이 준비를 해야 시작할 수 있습니다.");
                    }
                }
                else{
                    roomNoticePanel.setText("방장이 게임을 시작할 수 있습니다.");
                }
            }
        });


        panel_19.add(startButton, BorderLayout.CENTER);

        JPanel panel_20 = new JPanel();
        panel_12.add(panel_20);
        panel_20.setLayout(new BorderLayout(0, 0));

        JPanel panel_24 = new JPanel();
        panel_24.setPreferredSize(new Dimension(10, 30));
        panel_24.setBackground(new Color(223,241,239));
        panel_20.add(panel_24, BorderLayout.SOUTH);

        JPanel panel_29 = new JPanel();
        panel_29.setPreferredSize(new Dimension(30, 10));
        panel_29.setBackground(new Color(223,241,239));
        panel_20.add(panel_29, BorderLayout.WEST);

        JPanel panel_30 = new JPanel();
        panel_30.setPreferredSize(new Dimension(30, 10));
        panel_30.setBackground(new Color(223,241,239));
        panel_20.add(panel_30, BorderLayout.EAST);

        JPanel panel_32 = new JPanel();
        panel_32.setPreferredSize(new Dimension(10, 15));
        panel_32.setBackground(new Color(223,241,239));
        panel_20.add(panel_32, BorderLayout.NORTH);

        //ready button
        ImageIcon ready=new ImageIcon(new ImageIcon("asset/images/ready.png").getImage());
        readyButton = new JButton("New button");
        readyButton.setIcon(ready);
        readyButton.setBackground(new Color(223, 241, 239));
        readyButton.setBorder(new EmptyBorder(0,0,0,0));
        readyButton.setContentAreaFilled(false);
        readyButton.setFocusPainted(false);
        readyButton.setContentAreaFilled(false);
        readyButton.setOpaque(true);

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(gameController.getIndex() != 0){
                    gameController.changeReadyState();
                }
            }
        });



        panel_20.add(readyButton, BorderLayout.CENTER);

        JPanel panel_13 = new JPanel();
        RoomPanel.add(panel_13, BorderLayout.CENTER);
        panel_13.setLayout(new BorderLayout(0, 0));

        JPanel panel_14 = new JPanel();
        panel_14.setPreferredSize(new Dimension(50, 10));
        panel_14.setBackground(new Color(223,241,239));
        panel_13.add(panel_14, BorderLayout.WEST);

        JPanel panel_15 = new JPanel();
        panel_15.setPreferredSize(new Dimension(50, 10));
        panel_15.setBackground(new Color(223,241,239));
        panel_13.add(panel_15, BorderLayout.EAST);

        JPanel panel_16 = new JPanel();
        panel_16.setPreferredSize(new Dimension(10, 50));
        panel_16.setBackground(new Color(223,241,239));
        panel_13.add(panel_16, BorderLayout.NORTH);

        JPanel panel_17 = new JPanel();
        panel_17.setPreferredSize(new Dimension(10, 50));
        panel_17.setBackground(new Color(223,241,239));
        panel_13.add(panel_17, BorderLayout.SOUTH);

        JPanel panel_18 = new JPanel();
        panel_13.add(panel_18, BorderLayout.CENTER);
        panel_18.setBackground(new Color(223,241,239));
        panel_18.setLayout(new GridLayout(2, 2, 50, 20));


        for(int i = 0 ; i < 2 ; i ++) {
            JPanel roomRowPanel = new JPanel();
            FlowLayout fl_playerRoomRow1 = (FlowLayout) roomRowPanel.getLayout();
            fl_playerRoomRow1.setHgap(30);
            panel_18.add(roomRowPanel);
            roomRowPanel.setBackground(new Color(223, 241, 239));

            for(int j = 0 ; j < 2 ; j ++) {
                JPanel RoomPlayerPanel = new JPanel();
                roomPlayerPanel[i * 2 + j] = RoomPlayerPanel;
                RoomPlayerPanel.setBackground(new Color(0, 144, 81));
//                RoomPlayerPanel.setBackground(Color.cyan);
                JPanel tmpPanel = new JPanel();
                tmpPanel.setBackground(new Color(223,241,239));
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
                tmp1.setBackground(new Color(223,241,239));

                JPanel tmp2 = new JPanel();
                tmp2.setPreferredSize(new Dimension(110, 10));
                tmp1.add(tmp2, BorderLayout.WEST);
                tmp2.setBackground(new Color(223,241,239));

                JPanel tmp3 = new JPanel();
                tmp3.setPreferredSize(new Dimension(110, 10));
                tmp1.add(tmp3, BorderLayout.EAST);
                tmp3.setBackground(new Color(223,241,239));

                ImageIcon empProfileIcon=new ImageIcon(new ImageIcon("asset/images/emptyProfile.png").getImage());
                JLabel playerImageLabel = new JLabel();
                playerImageLabel.setIcon(empProfileIcon);
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