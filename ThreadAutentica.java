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
class ThreadAutentica extends Thread{
  private Map<String,Cliente> clientes = new HashMap<>();
  private Map<String,Informacao> servidores = new HashMap<>();
  private GestorReservas gestorRes;
  Socket cs;

  public ThreadAutentica(Socket cs,Map<String,Informacao> servidores, Map<String,Cliente> clientes,GestorReservas gestorRes){
    this.cs = cs;
    this.clientes = clientes; 
    this.servidores = servidores;
    this.nReserva = 1;
    this.gestorRes = gestorRes;
  }
  public void run(){
    Cliente cliente = new Cliente();
    Informacao info = new Informacao();
    int numero = -1;
    int uso = 0;
    boolean b = false;
    int escolha = -1;
    int rollback = 0;
    try{
       // faz escrita e flush automágicamente na conecção
       PrintWriter pw = new PrintWriter(cs.getOutputStream(),true);
       // faz leitura da comunicação
       BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
       b = true;
       try{
          while(b) {
               pw.println("\nEscolha uma opção:");
               pw.println("0-sair");
               pw.println("1-Registar-se como novo cliente.");
               pw.println("2-Autenticar-se como cliente.");
               pw.println("fim");
               String s = br.readLine();
               escolha = Integer.parseInt(s);
               if(escolha == 0){  // cliente desconectado
                 System.out.println("Cliente desconectou-se");
                 pw.println("A terminar ligação, adeus.");
                 b = false;
               } else if(escolha == 1){ // registar novo cliente
                   b = true;
                   pw.println("\nPode inserir 0 caso não pretenda registar-se em qualquer fase do registo:");
                   // regista novo cliente
                   int x = this.clientes.size() + 1;
                   String pass = null;
                   String email = null;
                   while(b){
                    pw.println("Insira o seu email:");
                    pw.println("fim");
                    email = br.readLine();
                    if(email.length()>0 && email.contains("@") && !clientes.containsKey(email)){
                      break;
                    } else{
                      try{
                          if(Integer.parseInt(email) == 0){
                              rollback = 1;
                              break;
                            }
                          } catch(NumberFormatException e){
                              pw.println("\nInsira um email válido (Existente).");
                          }
                      pw.println("\nInsira uma email válido por favor.");
                    }
                   }
                   if(rollback == 1){
                    rollback = 0;
                    break;
                   }
                   while(b){
                    pw.println("Insira a sua nova password:");
                    pw.println("fim");
                    pass = br.readLine();
                    if(pass.length()>0 && !pass.equals("0")){
                      clientes.put(email,new Cliente(x,pass,email,new HashMap<Integer,Reserva>(),0));
                      break;
                    } else{
                      try{
                            if(Integer.parseInt(pass) == 0){
                              break;
                            }
                          } catch(NumberFormatException e){
                            pw.println("Insira uma Password válida.");
                          }
                      pw.println("\nInsira uma password não vazia por favor.");
                    }
                   }
               } else if(escolha == 2){ // entrar como cliente
                  while(b){ // fase de autenticação
                      // 1º recebe userId
                      pw.println("\nPode inserir 0 caso não pretenda entrar na sua conta em qualquer fase da autenticação:");
                      pw.println("\nEmail:");
                      pw.println("fim");
                      try{
                        String email = br.readLine();
                        if(!this.clientes.containsKey(email)){
                          try{
                            if(Integer.parseInt(email) == 0){
                              rollback = 1;
                              break;
                            }
                          } catch(NumberFormatException e){
                              pw.println("\nInsira um email válido (Existente).");
                          }
                        } else{
                           cliente = clientes.get(email);
                           break;
                        } 
                      } catch(NumberFormatException e){
                        pw.println("\nInsira um email válido por favor.");
                        System.out.println("Foi inserido algo que não 0 ou email.");
                      }
                  }
                  if(rollback == 1){
                    rollback = 0;
                    break;
                  }
                  while(b){ // user existe
                      //2º recebe password do user
                      pw.println("Password:");
                      pw.println("fim");
                      String pass = br.readLine();
                      if(!pass.equals(cliente.getPass())){
                        try{
                            if(Integer.parseInt(pass) == 0){
                              rollback = 1;
                              break;
                            }
                          } catch(NumberFormatException e){
                            pw.println("Insira uma Password válida.");
                          }
                      } else break;
                  }
                  if(rollback == 1){
                    rollback = 0;
                    break;
                  }
                  while(true){
                        while(true){
                          //-> neste ponto cliente está autenticado
                          pw.println("\n\nEscolha uma opção:");
                          pw.println("0- Sair da conta.");
                          pw.println("1-Alugar um servidor.");
                          pw.println("2-Libertar um servidor.");
                          pw.println("3-Consultar divida.");
                          pw.println("fim");
                          try{
                            escolha = Integer.parseInt(br.readLine());
                            break;
                          } catch(NumberFormatException e){
                            pw.println("\nInsira um número válido.");
                          }
                        }
                        if(escolha == 0){
                          b = false;
                          pw.println("Desconectado.\nAdeus.");
                          break;
                        }
                        if(escolha == 1){ // Alugar servidor
                          String nomeServidor = null;
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
                          pw.println("\nPode inserir 0 caso deseje voltar ao menu inicial.");
                          pw.println("\n\nIndique o nome do servidor que prentende utilizar.");
                          pw.println("fim");
                          while(b){ // escolher tipo de servidor
                           nomeServidor = br.readLine();
                           if(!servidores.containsKey(nomeServidor)){
                            try{
                              if(Integer.parseInt(nomeServidor) == 0){
                                rollback = 1;
                                break;
                              }
                            } catch(NumberFormatException e){
                              pw.println("Insira um nome de servidor válido");
                              pw.println("fim");
                            }
                           } else{
                            info = servidores.get(nomeServidor);
                            break;
                           }
                          } // servidor escolhido e possível de usar
                          // escolher tipo de reserva --> possivelmente faz 
                          while(b && rollback == 0){ 
                           pw.println("Indique o tipo de reserva que pretende fazer :");
                           pw.println("Reservar servidor -> Insira 1 ");
                           pw.println("Ir a leilão por servidor -> Insira 2");
                           pw.println("fim");
                           try{
                            uso = Integer.parseInt(br.readLine());
                            if(uso != 1 && uso != 2){
                              if(uso == 0){
                                rollback = 1;
                                break;
                              }
                              pw.println("Insira um tipo de reserva válida.");
                              pw.println("fim");
                            } else{
                              // verificação para reserva
                              if(uso == 1 && (info.getLivresReserva() > 0 || info.getLivresLeilao() > 0 || info.getOcupadosLeilao() > 0)) break;
                              // verificação para leilão
                              if(uso == 2) break;
                            }
                           } catch(NumberFormatException e){
                            pw.println("Insira um número válido.");
                           }
                          } // tipo de reserva escolhido e possível até ao momento
                          // leiloar servidor
                          if(uso == 2 && rollback == 0){ // licitar
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
                              info.l.lock();
                              // licitação tem de ser positiva
                              if(info.reservaLicitacao(cliente.getId(),licitacao,gestorRes.getNReserva(),cliente.getEmail()) >= 0){
                                  // efetua reserva e adiciona ao cliente
                                  cliente.addReserva(gestorRes.getNReserva(),new Reserva(nomeServidor,licitacao));
                                  gestorRes.incNReserva();
                                  pw.println(cliente.cliente2String());
                              }
                            } catch(IOException e){
                                pw.println("Erro ao introduzir valor. Reserva cancelada.");
                                pw.println("fim");
                            } finally{
                              info.l.unlock();
                            }
                          }
                          if(uso == 1 && rollback == 0){ // reservar
                            try{
                              info.l.lock(); // isto serve caso entre no else if (impede descrepancias)
                              // tenho servidores para reserva normal
                              if(info.reservaNormal(cliente.getId(),gestorRes.getNReserva()) >= 0){
                                // efetua reserva e adiciona ao cliente
                                cliente.addReserva(gestorRes.getNReserva(),new Reserva(nomeServidor,info.getPreco()));
                                gestorRes.incNReserva();
                              }
                              // problema concorrencia 1º servidor 2º cliente
                              else if((numero = info.isPossible()) >= 0){
                                 info.mudaDono(nomeServidor,numero,cliente.getId(),clientes);
                              }
                              else{
                                 pw.println("Pedimos desculpa pelo inconveniente, mas de momento não há servidores disponíveis deste tipo.");
                                 pw.println("fim");
                              }
                             } finally{
                               info.l.unlock();
                             }
                             
                          }
                        }
                        else if(escolha == 2){ // libertar servidores alocados
                             while(b){
                               Map<Integer,Reserva> reservas = cliente.getReservas();
                              if(reservas.size() == 0){
                                pw.println("Não possui reservas para libertar.");
                                break;
                              }
                                pw.println("\n\nAs suas reservas:");
                                for(Map.Entry<Integer,Reserva> r : reservas.entrySet()){
                                   pw.println(" nº da reserva : " + r.getKey() + " --> { Nome do Servidor: " + r.getValue().getNome() +  ", Preço : " + r.getValue().getPreco() + ", Tempo da Reserva : " + r.getValue().getTReserva() + "}");
                                 }
                                 int i;
                                 while(true){
                                  try{
                                    pw.println("\nIndique a reserva que pretende terminar:");
                                    pw.println("Se pretende voltar atrás insira 0");
                                    pw.println("fim");
                                    i = Integer.parseInt(br.readLine());
                                    break;
                                  } catch(NumberFormatException e){
                                    pw.println("\nInsira um número por favor.");
                                  }

                                 }
                                 if(i == 0){
                                  pw.println("\nA sair...");
                                  break;
                                 }
                                 while(b){
                                   // encontrar tipo de servidor e fazer lock()
                                   for(Map.Entry<String,Informacao> tipoServidor : servidores.entrySet()){
                                    if(tipoServidor.getValue().getReservas().containsKey(i)){
                                      info = tipoServidor.getValue();
                                      break;
                                    }
                                   }
                                   try{
                                    info.l.lock();
                                    cliente.l.lock(); 
                                    if(reservas.containsKey(i)){
                                      // aumenta a divida do cliente
                                      cliente.growDivida(i);
                                      // apaga a reserva do cliente
                                      cliente.removeReserva(i);
                                      //servidor apagado pronto para reservar
                                      info.removeReserva(i,this.clientes);
                                      break;
                                    }else {
                                      pw.println("A reserva inserida não lhe pertence, tente outra vez.");
                                      break;
                                    }
                                   } finally{
                                      info.l.unlock();
                                      cliente.l.unlock();
                                   }
                                 }
                             }
                        }
                        else if(escolha == 3){ // consultar divida da conta
                           pw.println(cliente.getDivida());
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
