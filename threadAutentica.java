import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.*;
import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

// aceitam clientes e direcionam para servidores que estejam vazios
class threadAutentica extends Thread{
  private Map<Integer,Cliente> clientes = new HashMap<>();
  private Map<String,Informacao> servidores = new HashMap<>();
  Socket cs;
  public threadAutentica(Socket cs,Map<String,Informacao> servidores, Map<Integer,Cliente> clientes){
    this.cs = cs;
    for(Map.Entry<String,Informacao> servidor : servidores.entrySet()){
      this.servidores.put(servidor.getKey(),servidor.getValue());
      }
    for(Map.Entry<Integer,Cliente> cliente : clientes.entrySet()){
      this.clientes.put(cliente.getKey(),cliente.getValue());
    }
  }
  public void run(){
    Cliente cliente = new Cliente();
    Informacao info = new Informacao();
    int numero = -1;
    int uso = 0;
    boolean b = false;
    int escolha = -1;
    try{
       // faz escrita e flush automágicamente na conecção
       PrintWriter pw = new PrintWriter(cs.getOutputStream(),true);
       // faz leitura da comunicação
       BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
       b = true;
       try{
          while(b) {
               pw.println("Escolha uma opção:");
               pw.println("0-sair");
               pw.println("1-Registar-se como novo cliente.");
               pw.println("2-Autenticar-se como cliente.");
               pw.println("fim");
               String s = br.readLine();
               escolha = Integer.parseInt(s);
               if(escolha == 0){  // cliente desconectado
                 pw.println("A terminar ligação, adeus.");
                 b = false;
               } else if(escolha == 1){ // registar novo cliente
                   b = true;
                   // regista novo cliente
                   while(b){
                    int x = clientes.size()+1;
                    pw.println("O seu id é : " + x);
                    pw.println("Insira a sua nova pass:");
                    pw.println("fim");
                    String pass = br.readLine();
                    clientes.put(x,new Cliente(x,pass,new HashMap<Integer,Reserva>(),0));
                    break;
                   }
               } else if(escolha == 2){ // entrar como cliente
                  while(true){
                        while(b){ // fase de autenticação
                          // 1º recebe userId
                          pw.println("UserId:");
                          pw.println("fim");
                          int userId = Integer.parseInt(br.readLine());
                          if(!this.clientes.containsKey(userId)){
                           pw.println("Insira um UserId válido.");
                           pw.println("fim");
                          } else{
                           cliente = clientes.get(userId);
                           break;
                          } 
                        }
                        while(b){ // userId existe
                          //2º recebe password do user
                          pw.println("Password:");
                          pw.println("fim");
                          String pass = br.readLine();
                          if(!pass.equals(cliente.getPass())){
                           pw.println("Insira uma Password válida.");
                           pw.println("fim");
                          } else break;
                        } 
                        pw.println(cliente.cliente2String());
                        //-> neste ponto cliente está autenticado
                        pw.println("0- Sair da conta.");
                        pw.println("1-Alugar um servidor.");
                        pw.println("2-Libertar um servidor.");
                        pw.println("3-Consultar saldo.");
                        pw.println("fim");
                        escolha = Integer.parseInt(br.readLine());
                        if(escolha == 0){
                          b = false;
                          pw.println("Desconectado.Adeus !)");
                          break;
                        }
                        if(escolha == 1){
                          // apresenta todos os servidores e quais os disponíveis e ocupados
                          for(Map.Entry<String,Informacao> servidor : servidores.entrySet()){
                            info = servidor.getValue();
                            pw.println("Nome : " + servidor.getKey());
                            pw.println("Livres para leilão : " + info.getLivresLeilao());
                            pw.println("Livres para reserva : " + info.getLivresReserva());
                            pw.println("Ocupados leilão  : " + info.getOcupadosLeilao());
                            pw.println("Ocupados reserva : " + info.getOcupadosReserva());
                            pw.println("Preço : " + info.getPreco());
                            pw.println("//--------------------------------//");
                          }
                          pw.println("\n\nIndique o nome do servidor que prentende utilizar.");
                          pw.println("fim");
                          while(b){ // escolher tipo de servidor
                           String nomeServidor = br.readLine();
                           if(!servidores.containsKey(nomeServidor)){
                             pw.println("Insira um nome de servidor válido");
                             pw.println("fim");
                           } else {// verificar se há servidores disponíveis desse tipo
                             info = servidores.get(nomeServidor);
                             if(info.getServidoresLivres() > 0){ break;
                             } else{
                               pw.println("Servidores indiponíveis para reserva, aguarde e tente mais tarde.");
                               pw.println("fim");
                             } 
                           }
                          } // servidor escolhido e possível de usar
                          // escolher tipo de reserva --> possivelmente faz 
                          while(b){ 
                           pw.println("Indique o tipo de reserva que pretende fazer :");
                           pw.println("Reservar servidor -> Insira 1 ");
                           pw.println("Ir a leilão por servidor -> Insira 2");
                           pw.println("fim");
                           uso = Integer.parseInt(br.readLine());
                           if(uso != 1 && uso != 2){
                             pw.println("Insira um tipo de reserva válida.");
                             pw.println("fim");
                           } else{
                             // verificação para reserva
                             if(uso == 1 && (info.getLivresReserva() > 0 || info.getLivresLeilao() > 0)) break;
                             // verificação para leilão
                             if(uso == 2 && info.getLivresLeilao() > 0) break;
                           }
                          } // tipo de reserva escolhido e possível até ao momento
                          // leiloar servidor
                          if(uso == 2){ // licitar
                            try{
                              pw.println("Insira valor que está disposto a pagar:");
                              pw.println("fim");
                              double licitacao = -1;
                              while(licitacao <= 0){
                               licitacao = Double.parseDouble(br.readLine());
                               if(licitacao <= 0){
                                 pw.println("Insira um valor válido.");
                                 pw.println("fim");
                               }
                              }
                              // licitação tem de ser positiva
                              if((numero = info.reservaLicitacao(cliente.getId(),licitacao)) >= 0){
                                  // efetua reserva e adiciona ao cliente
                                  pw.println("Passou");
                                  pw.println(cliente.cliente2String());
                                  cliente.addReserva(numero,new Reserva(licitacao));
                                  pw.println(cliente.toString());
                              }
                            } catch(IOException e){
                                pw.println("Erro ao introduzir valor. Reserva cancelada.");
                                pw.println("fim");
                            }
                          }
                          if(uso == 1){ // reservar
                             // tenho servidores para reserva normal
                             if((numero = info.reservaNormal(cliente.getId())) >= 0){
                               // efetua reserva e adiciona ao cliente
                               cliente.addReserva(numero,new Reserva(info.getPreco()));
                             }
                             // não tenho servidores de reserva normal
                             else if((numero = info.reservaLicitacao(cliente.getId(),info.getPreco())) >= 0){
                                // efetua reserva e adiciona ao cliente
                                cliente.addReserva(numero,new Reserva(info.getPreco()));
                             }
                             else if((numero = info.isPossible()) >= 0){
                                info.mudaDono(numero,cliente.getId(),clientes);
                                // efetua reserva e adiciona ao cliente
                                cliente.addReserva(numero,new Reserva(info.getPreco()));
                             }
                             else{
                                pw.println("Pedimos desculpa pelo inconveniente, mas de momento não há servidores disponíveis deste tipo.");
                                pw.println("fim");
                             }
                          }
                        }
                        else if(escolha == 2){ // libertar servidores alocados
                           try{
                             cliente.l.lock();
                             while(b){
                                Map<Integer,Reserva> reservas = cliente.getReservas();
                                for(Map.Entry<Integer,Reserva> r : reservas.entrySet()){
                                   pw.println(r.getKey());
                                 }
                                 while(b){
                                   pw.println("Indique a reserva que pretende terminar:");
                                   pw.println("Se pretende voltar atrás insira -1");
                                   pw.println("fim");
                                   int i = Integer.parseInt(br.readLine());
                                   if(i == -1){
                                     break;
                                   }
                                   if(reservas.containsKey(i)){
                                     // aumenta a divida do cliente
                                     cliente.growDivida(reservas.get(i).getTReserva());
                                     // apaga a reserva do cliente
                                     cliente.removeReserva(i);
                                     //servidor apagado pronto para reservar
                                     info.removeReserva(i,this.clientes);
                                   } else {
                                     pw.println("A reserva inserida não lhe pertence, tente outra vez.");
                                     pw.println("fim");
                                   }
                                 }
                             }
       
                           } finally{
                             cliente.l.unlock();
                           }
                        }
                        else if(escolha == 3){ // consultar divida da conta
                           pw.println(cliente.getDivida());
                           pw.println("fim");
                        }
                        else{
                           pw.println("Escolha uma opção válida.");
                           pw.println("fim");
                        }
                  }
               } else {
                 pw.println("A escolha inserida não é válida.");
               }
             }
       } catch(IOException e){
             System.out.println("Cliente terminou conecção!!");
       } finally{
             cs.close();
       }
    } catch(IOException e){
             System.out.println("Erro ao fechar socket!!");
    }
  }
}
