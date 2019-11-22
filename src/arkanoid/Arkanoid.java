package arkanoid;

import java.awt.*;
import java.applet.Applet;

public class Arkanoid extends Applet implements Runnable {

    Dimension d;
    Font largefont = new Font("Helvetica", Font.BOLD, 24);
    Font smallfont = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics fmsmall, fmlarge;
    Graphics goff;
    Image ii;
    Thread thethread;
    boolean ingame = false;
    int player1score;
    int ballx, bally;
    int batpos;
    int batdpos = 0;
    int balldx = 0, balldy = 0;
    int dxval;
    int ballsleft;
    int count;
    boolean showtitle = true;
    boolean[] showbrick;
    int bricksperline;
    final int borderwidth = 5;
    final int batwidth = 30;
    final int ballsize = 15;
    final int batheight = 5;
    final int scoreheight = 20;
    final int screendelay = 300;
    final int brickwidth = 15;
    final int brickheight = 8;
    final int brickspace = 2;
    final int backcol = 0x102040;
    final int numlines = 4;
    final int startline = 32;

    @Override
    public String getAppletInfo() {
        return ("Arkanoid");
    }

    @Override
    public void start() {
        Graphics g;
        d = size();
        setBackground(new Color(backcol));
        bricksperline = (d.width - 2 * borderwidth) / (brickwidth + brickspace);
        d.width = bricksperline * (brickwidth + brickspace) + (2 * borderwidth);
        g = getGraphics();
        g.setFont(smallfont);
        fmsmall = g.getFontMetrics();
        g.setFont(largefont);
        fmlarge = g.getFontMetrics();
        showbrick = new boolean[bricksperline * numlines];
        GameInit();
        if (thethread == null) {
            thethread = new Thread(this);
            // vai para run
            thethread.start();
        }
    }

    public void GameInit() {
        // posicao do curso
        batpos = (d.width - batwidth) / 2;
        // posicao horizontal da bola
        ballx = (d.width - ballsize) / 2;
        // posico vertical da bola
        bally = (d.height - ballsize - scoreheight - 2 * borderwidth);
        player1score = 0;
        ballsleft =4;
        dxval = 2;
        // define se direita ou esquerda
        if (Math.random() < 0.5) {
            balldx = dxval;
        } else {
            balldx = -dxval;
        }
        // define para cima
        balldy = -dxval;
        count = screendelay;
        batdpos = 0;
        InitBricks();
    }

    public void InitBricks() {
        int i;
        // ajusta as posicoes LÓGICAS dos blocos
        for (i = 0; i < numlines * bricksperline; i++) {
            showbrick[i] = true;
        }
    }

    @Override
    public boolean keyDown(Event e, int key) {
        if (ingame) {
            if (key == Event.LEFT) {
                batdpos = -2;
            }
            if (key == Event.RIGHT) {
                batdpos = 2;
            }
            if (key == Event.ESCAPE) {
                ingame = false;
            }
        } else {
            if (key == 'i' || key == 'I') {
                ingame = true;
                GameInit();
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(Event e, int key) {
        System.out.println("Key: " + key);
        if (key == Event.LEFT || key == Event.RIGHT) {
            batdpos = 0;
        }
        return true;
    }

    @Override
    public void paint(Graphics g) {
        String s;
        Graphics gg;

        if (goff == null && d.width > 0 && d.height > 0) {
            ii = createImage(d.width, d.height);
            goff = ii.getGraphics();
        }
        if (goff == null || ii == null) {
            return;
        }

        goff.setColor(new Color(backcol));
        goff.fillRect(0, 0, d.width, d.height);
        // verifica se o jogo está ativo ou não
        if (ingame) {
            PlayGame();
        } else {
            ShowIntroScreen();
        }
        g.drawImage(ii, 0, 0, this);
    }

    public void PlayGame() {
        MoveBall();
        CheckBat();
        CheckBricks();
        DrawPlayField();
        DrawBricks();
        ShowScore();
    }

    public void ShowIntroScreen() {
        String s;

        MoveBall();
        CheckBat();
        CheckBricks();
        BatDummyMove();
        DrawPlayField();
        DrawBricks();
        ShowScore();
        goff.setFont(largefont);
        goff.setColor(new Color(96, 128, 255));
        // desenha texto introdutório
        if (showtitle) {
            s = "Arkanoid";
            goff.drawString(s, (d.width - fmlarge.stringWidth(s)) / 2, (d.height - scoreheight - borderwidth) / 2 - 20);
        } else {
            goff.setFont(smallfont);
            goff.setColor(new Color(96, 128, 255));
            s = "'I' Para Iniciar Jogo";
            goff.drawString(s, (d.width - fmsmall.stringWidth(s)) / 2, (d.height - scoreheight - borderwidth) / 2 - 10);
            goff.setColor(new Color(255, 160, 64));
            s = "Use os Cursores para Mover";
            goff.drawString(s, (d.width - fmsmall.stringWidth(s)) / 2, (d.height - scoreheight - borderwidth) / 2 + 20);
        }
        count--;
        if (count <= 0) {
            count = screendelay;
            showtitle = !showtitle;
        }
    }

    // desenha os tijolos
    public void DrawBricks() {
        int i, j;
        // assume que nao existem tijolos (prova por contradicao)
        boolean nobricks = true;
        // define degradee (pode dar divisao por zero)
        int colordelta = 255 / (numlines - 1);

        // desenha linha
        for (j = 0; j < numlines; j++) {
            // desenha colunas
            for (i = 0; i < bricksperline; i++) {
                // verifica condicao logica do tijolo
                if (showbrick[j * bricksperline + i]) {
                    // assume que ainda existe pelo menos um tijolo
                    nobricks = false;
                    // desenha o tijolo no buffer de video
                    goff.setColor(new Color(255, j * colordelta, 255 - j * colordelta));
                    goff.fillRect(borderwidth + i * (brickwidth + brickspace), startline + j * (brickheight + brickspace),
                            brickwidth, brickheight);
                }
            }
        }
        // se nao existirem mais tijolos inicializa todos os tijolos
        if (nobricks) {
            InitBricks();
            // se "em jogo" adiciona 100 pontos ao placar
            if (ingame) {
                player1score += 100;
            }
        }
    }

    // desenha as bordas, cursor e bola
    public void DrawPlayField() {
        goff.setColor(Color.white);
        // desenha borda horizontal superior
        goff.fillRect(0, 0, d.width, borderwidth);
        // desenha borda vertical (esquerda)
        goff.fillRect(0, 0, borderwidth, d.height);
        // desenha borda vertical (direita)
        goff.fillRect(d.width - borderwidth, 0, borderwidth, d.height);
        // desenha o cursor
        goff.fillRect(batpos, d.height - 2 * borderwidth - scoreheight, batwidth, batheight); // bat
        // desenha a bola
        goff.fillRect(ballx, bally, ballsize, ballsize);
    }

    // desenha o placar e status
    public void ShowScore() {
        String s;
        // define a fonte e a cor
        goff.setFont(smallfont);
        goff.setColor(Color.white);
        // prepara a string
        s = "Placar: " + player1score;
        // desenha a string (a 40 pixeis da esquerda)
        goff.drawString(s, 40, d.height - 5);
        // prepara nova string
        s = "Bolas: " + ballsleft;
        // desenha a string (a 40 pixeis da direita - tamanho da string)
        goff.drawString(s, d.width - 40 - fmsmall.stringWidth(s), d.height - 5);
    }

    public void MoveBall() {
        // define direcao horizontal e vertical
        ballx += balldx;
        bally += balldy;
        // se bater na borda esquerda
        if (bally <= borderwidth) {
            // inverte movimento
            balldy = -balldy;
            bally = borderwidth;
        }
        // se passar do cursor
        if (bally >= (d.height - ballsize - scoreheight)) {
            if (ingame) {
                ballsleft--;
                // acaba jogo
                if (ballsleft <= 0) {
                    ingame = false;
                }
            }
            // zera posicao horizontal e vertical
            ballx = batpos + (batwidth - ballsize) / 2;
            bally = startline + numlines * (brickheight + brickspace);
            // ajusta direcao
            balldy = dxval;
            balldx = 0;
        }
        // se bater na borda direita
        if (ballx >= (d.width - borderwidth - ballsize)) {
            // inverte o movimento
            balldx = -balldx;
            // ajusta posicao
            ballx = d.width - borderwidth - ballsize;
        }
        // se bater na borda superior
        if (ballx <= borderwidth) {
            // ajusta movimento
            balldx = -balldx;
            ballx = borderwidth;
        }
    }

    public void BatDummyMove() {
        // IA Fajuta
        // se a bola esta a esquerda do cursor
        if (ballx < (batpos + 2)) {
            batpos -= 3;
        } // se a bola esta a direita do cursor
        else if (ballx > (batpos + batwidth - 3)) {
            batpos += 3;
        }
    }

    public void CheckBat() {
        // move cursor
        batpos += batdpos;

        // impede que o cursor passe pelas bordas
        if (batpos < borderwidth) {
            batpos = borderwidth;
        } else if (batpos > (d.width - borderwidth - batwidth)) {
            batpos = (d.width - borderwidth - batwidth);
        }

        if (bally >= (d.height - scoreheight - 2 * borderwidth - ballsize)
                && bally < (d.height - scoreheight - 2 * borderwidth)
                && (ballx + ballsize) >= batpos && ballx <= (batpos + batwidth)) {
            bally = d.height - scoreheight - ballsize - borderwidth * 2;
            balldy = -dxval;
            balldx = CheckBatBounce(balldx, ballx - batpos);
        }
    }

    public int CheckBatBounce(int dy, int delta) {
        int sign;
        int stepsize, i = -ballsize, j = 0;
        stepsize = (ballsize + batwidth) / 8;

        if (dy > 0) {
            sign = 1;
        } else {
            sign = -1;
        }

        while (i < batwidth && delta > i) {
            i += stepsize;
            j++;
        }
        switch (j) {
            case 0:
            case 1:
                return -4;
            case 2:
                return -3;
            case 7:
                return 3;
            case 3:
            case 6:
                return sign * 2;
            case 4:
            case 5:
                return sign * 1;
            default:
                return 4;
        }
    }

    // verifica de a bola bateu eu algum tijolo
    public void CheckBricks() {
        int i, j, x, y;
        // declara uma variavel auxiliar e atribui a direcao horizontal da bola
        int xspeed = balldx;
        // mantem a direcao sempre positiva (esquerda para direita)
        if (xspeed < 0) {
            xspeed = -xspeed;
        }
        // declara outra variavel auxiliar e atrtibui a direcao vertical da bola
        int ydir = balldy;
        // se a bola esta acima ou abaixo da linha de tijolos sai da funcao
        if (bally < (startline - ballsize) || bally > (startline + numlines * (brickspace + brickheight))) {
            return;
        }
        // inicia loop de verificacao
        for (j = 0; j < numlines; j++) {
            for (i = 0; i < bricksperline; i++) {
                // se o tijolo existe, verifica se a bola bateu nele
                if (showbrick[j * bricksperline + i]) {
                    // define a posicao vertical do tijolo
                    y = startline + j * (brickspace + brickheight);
                    // define a posicao horizontal do tijolo
                    x = borderwidth + i * (brickspace + brickwidth);
                    // verifica se bateu no tijolo
                    // (em cima, em baixo, no lado esquerdo ou no lado direito
                    if (bally >= (y - ballsize) && bally < (y + brickheight)
                            && ballx >= (x - ballsize) && ballx < (x + brickwidth)) {
                        // se bateu, exclui o tijolo do vetor de tijolos
                        showbrick[j * bricksperline + i] = false;
                        // se "em jogo" incrementa o placar
                        //(quanto mais alto o tijolo maior o incremento)
                        if (ingame) {
                            player1score += (numlines - j);
                        }
                        // se bateu de lado esquerdo
                        // ou se bateu em baixo na quina direita
                        if (ballx >= (x - ballsize) && ballx <= (x - ballsize + 3)) {
                            // vai para esquerda
                            balldx = -xspeed;
                        } // se bateu de lado direito
                        // ou se bateu em baixo na quina esquerda
                        else if (ballx <= (x + brickwidth - 1) && ballx >= (x + brickwidth - 4)) {
                            // vai para direita
                            balldx = xspeed;
                        }
                        // inverte posicao vertical
                        // tem uma falha de fisica aqui!
                        balldy = -ydir;
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        long starttime;
        Graphics g;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        g = getGraphics();

        while (true) {
            // marcador de velocidade
            starttime = System.currentTimeMillis();
            try {
                paint(g);
                starttime += 20;
                // define pausa de acordo com a velocidade da máquina
                Thread.sleep(Math.max(0, starttime - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void stop() {
        if (thethread != null) {
            thethread.stop();
            thethread = null;
        }
    }
}
