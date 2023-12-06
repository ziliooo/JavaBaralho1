// Iniciando o código
import java.util.*;

public class JogoDeCartas1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Jogo de Cartas!");

        // Solicita o número de jogadores
        System.out.print("Informe o número de jogadores (entre 3 e 6): ");
        int numJogadores = scanner.nextInt();

        // Verifica se o número de jogadores é valido
        if (numJogadores < 3 || numJogadores > 6) {
            System.out.println("Número de jogadores inválido. O jogo suporta de 3 a 6 jogadores.");
            return;
        }

        // Cria o jogo com o número de jogadores informado
        Jogo jogo = new Jogo(numJogadores);

        // Inicia o jogo
        jogo.iniciarJogo();

        // Executa as rodadas
        for (int rodada = 1; rodada <= 12; rodada++) {
            System.out.println("\n--- Rodada " + rodada + " ---");

            // Solicita a carta a ser jogada
            for (int jogador = 0; jogador < numJogadores; jogador++) {
                System.out.println("\nJogador " + (jogador + 1) + " - " + jogo.getNomeJogador(jogador) + ":");
                jogo.mostrarMao(jogador);
                System.out.print("Escolha a carta a ser jogada: ");
                int cartaEscolhida = scanner.nextInt();
                jogo.jogarCarta(jogador, cartaEscolhida);
            }

            // Imprime o tabuleiro e a pontuação dos jogadores
            jogo.imprimirEstadoAtual();
        }

        // Finaliza o jogo e printa o vencedor
        jogo.finalizarJogo();
    }

    static class Jogo {
        private List<List<Integer>> tabuleiro;
        private List<List<Integer>> maos;
        private List<Integer> pontuacao;
        private List<String> nomesJogadores;

        public Jogo(int numJogadores) {
            tabuleiro = new ArrayList<>();
            maos = new ArrayList<>();
            pontuacao = new ArrayList<>();
            nomesJogadores = new ArrayList<>();

            // Cria o tabuleiro
            for (int i = 0; i < 5; i++) {
                tabuleiro.add(new ArrayList<>());
            }

            // Começa as mãos dos jogadores e zera a pontuação
            for (int i = 0; i < numJogadores; i++) {
                maos.add(new ArrayList<>());
                pontuacao.add(0);
            }

            // Cria os jogadores
            criarJogadores(numJogadores);

            // Cria o baralho e dá as cartas
            List<Integer> baralho = criarBaralho();
            distribuirCartas(baralho);
        }

        // Cria os jogadores
        private void criarJogadores(int numJogadores) {
            Scanner scanner = new Scanner(System.in);

            for (int i = 0; i < numJogadores; i++) {
                System.out.print("Informe o nome do Jogador " + (i + 1) + ": ");
                String nomeJogador = scanner.nextLine();
                nomesJogadores.add(nomeJogador);
            }
        }

        // Cria o baralho
        private List<Integer> criarBaralho() {
            List<Integer> baralho = new ArrayList<>();
            for (int i = 1; i <= 109; i++) {
                baralho.add(i);
            }
            Collections.shuffle(baralho);
            return baralho;
        }

        // Dá as cartas
        private void distribuirCartas(List<Integer> baralho) {
            for (int i = 0; i < maos.size(); i++) {
                for (int j = 0; j < 12; j++) {
                    maos.get(i).add(baralho.remove(0));
                }
            }

            for (int i = 0; i < 5; i++) {
                tabuleiro.get(i).add(baralho.remove(0));
            }
        }

        public void iniciarJogo() {
            System.out.println("\n--- Início do Jogo ---");
            imprimirEstadoAtual();
        }

        // Valida se a carta é válida e joga se for, senão pede para informar uma válida
        public void jogarCarta(int jogador, int carta) {
            
            Scanner scanner = new Scanner(System.in);
            while (!maos.get(jogador).contains(carta)) {
                System.out.println("A carta " + carta + " não está na mão do jogador " + (jogador + 1) + ". Escolha uma carta válida:");
                carta = scanner.nextInt();
            }

            //Lógica de posicionar a carta
            int linha = escolherLinha(tabuleiro, carta);
            int coluna = escolherColuna(tabuleiro.get(linha), carta);
           
            // Posiciona a carta ao tabuleiro na posição determinada
            tabuleiro.get(linha).add(coluna, carta);

            // Remove a carta da mão do jogador
            maos.get(jogador).remove(Integer.valueOf(carta));

            // // Atualiza a pontuação do jogador
            // pontuacao.set(jogador, pontuacao.get(jogador) + calcularPontos(carta));

            // Verifica se a linha está cheia e coleta as cartas (se necessário)
            if (tabuleiro.get(linha).size() == 5) {
                
                pontuacao.set(jogador, pontuacao.get(jogador) + coletarCartas(linha));
                
                // Atualiza a pontuação do jogador
                pontuacao.set(jogador, pontuacao.get(jogador) + calcularPontos(carta));

            }

            // Printa a jogada 
            System.out.println("Jogador " + (jogador + 1) + " - " + getNomeJogador(jogador) + " jogou a carta " + carta);
        }


        private int escolherLinha(List<List<Integer>> tabuleiro, int carta) {
            // Lógica para escolher a linha correta com base nas regras informadas para o trabalho
            int linhaEscolhida = 0;
            int menorDiferenca = Integer.MAX_VALUE;

            for (int i = 0; i < tabuleiro.size(); i++) {
                List<Integer> linha = tabuleiro.get(i);

                if (linha.isEmpty() || carta > linha.get(linha.size() - 1)) {
                    int diferenca = carta - (linha.isEmpty() ? 0 : linha.get(linha.size() - 1));

                    if (diferenca < menorDiferenca) {
                        menorDiferenca = diferenca;
                        linhaEscolhida = i;
                    }
                }
            }

            return linhaEscolhida;
        }

        private int escolherColuna(List<Integer> linha, int carta) {
            // Lógica para escolher a coluna correta com base nas regras informadas para o trabalho
            int colunaEscolhida = 0;

            for (int i = 0; i < linha.size(); i++) {
                if (carta > linha.get(i)) {
                    colunaEscolhida = i + 1;
                } else {
                    break;
                }
            }

            return colunaEscolhida;
        }

        // Calcula os Pontos seguindo as regras do trabalho
        private int calcularPontos(int carta) {
            // Lógica para calcular os pontos com base nas regras
            int pontos = 1; // Pontuação base das cartas

            if (carta % 10 == 5) {
                pontos++; // Regra a = Cartas terminadas com o dígito 5 valem 1 ponto extra;
            }

            if (carta % 10 == 0) {
                pontos += 2; // Regra b = Cartas múltiplas de 10 valem 2 pontos extras;
            }

            if (temDigitosRepetidos(carta)) {
                pontos += 4; // Regra c = Cartas com dois dígitos repetidos valem 4 pontos extras;
            }

            return pontos;
        }

        private boolean temDigitosRepetidos(int carta) {
            // Verifica se a carta tem dois dígitos repetidos para acrescentar pontos
            String cartaStr = String.valueOf(carta);
            return cartaStr.length() == 3 && (cartaStr.charAt(0) == cartaStr.charAt(1) || cartaStr.charAt(1) == cartaStr.charAt(2));
        }

        private int coletarCartas(int linha) {
            // Coleta as cartas de uma linha cheia
            List<Integer> cartasColetadas = tabuleiro.get(linha);
            tabuleiro.get(linha).clear(); // Limpa a linha para seguir o jogo
            return cartasColetadas.stream().mapToInt(Integer::intValue).sum();
        }

        // Captura o nome do jogador
        public String getNomeJogador(int jogador) {
            return nomesJogadores.get(jogador);
        }

        // Mostra a mão do jogador
        public void mostrarMao(int jogador) {
            System.out.println("Mão do Jogador " + getNomeJogador(jogador) + ": " + maos.get(jogador));
        }

        // Printa o estado atual do tabuleiro e pontuação
        public void imprimirEstadoAtual() {
            System.out.println("\nTabuleiro:");
            for (int i = 0; i < tabuleiro.size(); i++) {
                List<Integer> linha = tabuleiro.get(i);
                System.out.print("Linha " + (i + 1) + ": ");
                for (int j = 0; j < linha.size(); j++) {
                    System.out.print(linha.get(j) + ", ");
                }
                System.out.println();
            }
            
            System.out.println();

            for (int i = 0; i < nomesJogadores.size(); i++) {
                System.out.println("Pontuação do Jogador " + (i + 1) + " - " + getNomeJogador(i) + ": " + pontuacao.get(i));
            }
        }

        // Finaliza o jogo ao acabar 
        public void finalizarJogo() {
            System.out.println("\n--- Fim do Jogo ---");

            // Lógica para calcular o vencedor do jogo
            int menorPontuacao = Collections.min(pontuacao);
            List<String> vencedores = new ArrayList<>();

            for (int i = 0; i < pontuacao.size(); i++) {
                if (pontuacao.get(i) == menorPontuacao) {
                    vencedores.add(getNomeJogador(i));
                }
            }

            // Imprime a pontuação individual de cada jogador ao final do jogo
            for (int i = 0; i < nomesJogadores.size(); i++) {
                System.out.println("Pontuação final do Jogador " + (i + 1) + " - " + getNomeJogador(i) + ": " + pontuacao.get(i));
            }

            // Imprime as cartas coletadas por cada jogador ao final do jogo
            for (int i = 0; i < maos.size(); i++) {
                System.out.println("Cartas coletadas por " + getNomeJogador(i) + ": " + maos.get(i));
            }

            // Imprime o jogador vencedor ou empate entre jogadores
            if (vencedores.size() == 1) {
                System.out.println("\nJogador vencedor: " + vencedores.get(0));
            } else {
                System.out.println("\nEmpate! Jogadores vencedores: " + String.join(", ", vencedores));
            }
        }
    }
}