# Arkanoid

## Alteração no jogo Arkanoid feito para a matéria de PROGRAMAÇÃO I

O código fornecido para elaboração da atividade contava apenas com as funções básicas do jogo, as seguintes alterações foram realizadas para melhorar sua dinâmica e jogabilidade:

- [x] Aumento na velocidade do batedor (keydown)
- [x] Aumento na altura dos blocos
- [x] Alterar cor dos blocos da primeira linha de cima
- [x] Para eliminar um bloco da primeira linha de cima, deve-se bater duas vezes (ponto só conta quando bloco é destruido)
- [x] Alterar cor dos blocos da primeira linha de cima quando batidos uma vez
- [x] Reduz o batedor a cada 10 pts
- [x] Aumento na velocidade do jogo
- [x] A tela desce a cada 12 segundos


Alterei a variavel showbrick de boolean pra int
Na função init, os blocos da primeira linha recebem valor 2, das outras linhas recebem 1
Se o bloco é acertado, a variavel é decrementada em 1 (função checkBricks)
Na função DrawBricks, se os blocos são da primeira linha eles recebem a cor cinza
Ainda na DrawBricks, se os blocos da primeira linha possuirem valor 1, ou seja, já tiverem sido acertados uma vez eles rcebem outra cor. 

Criei as variaveis pointWhereItChanged e batwidthChanged, na funcao CheckBat, quando a diferenca entre o palyerScore e o point whereItChanged == 10, batWidthChanged recebe false, e o batedor é reduzido. As variaveis sao reiniciadas na funcao GameInit

Clonei a função GameInit() e fiz a GameRestart(), que não zera os pontos e as bolas restantes.

A função CheckBlocksFall(), itera sobre as linhas de blocos, e confere se a linha está vazia, pra cada linha é definida uma altura, a cada 12 segundos a tela desce, se a ultima linha de baixo estiver em determinada altura o jogo acaba. (uso da System.currentTimeMillis())

Funções alteradas: DrawBricks(), paint(), ShowIntroScreen(), checkBat(), checkBricks(),
     GameInit(), initBricks(), keyDown();
Funções criadas:  GameRestart(), CheckBlocksFall();
