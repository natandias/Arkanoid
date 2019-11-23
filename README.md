# Arkanoid

## Alteração no jogo Arkanoid feito para a matéria de PROGRAMAÇÃO I

O código fornecido para elaboração da atividade contava apenas com as funções básicas do jogo, as seguintes alterações foram realizadas para melhorar sua dinâmica e jogabilidade:

- [x] Aumento na velocidade do batedor
- [x] Aumento na altura dos blocos
- [x] Alterar cor dos blocos da primeira linha de cima
- [x] Para eliminar um bloco da primeira linha de cima, deve-se bater duas vezes (ponto só conta quando bloco é destruido)
- [x] Alterar cor dos blocos da primeira linha de cima quando batidos uma vez
- 

Alterei a variavel showbrick de boolean pra int
Na função init, os blocos da primeira linha recebem valor 2, das outras linhas recebem 1
Se o bloco é acertado, a variavel é decrementado em 1 (função checkBricks)
Na função DrawBricks, se os blocos são da primeira linha eles recebem a cor cinza
Ainda na DrawBricks, se os blocos da primeira linha possuirem valor 1, ou seja, já tiverem sido acertados uma vez eles rcebem outra cor. 


S - Slow: slows down the energy ball.
L - Laser: enables the Vaus to fire laser beams.
C - Catch: catches the energy ball and shoots it when you want it.
B - Break: allow player to move to next playfield.
E - Expand: expands the Vaus.
P - Player: gains an additional Vaus.
D - Disrupt: splits the energy ball into three particles.
