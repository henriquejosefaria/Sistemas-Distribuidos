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


class Servidores{

  public static void main(String[] args) throws Exception {
    Map<String,Cliente> clientes = new HashMap<>();
    Map<String,Informacao> servidores = new HashMap<>();
    ArrayList <String> nomes = new ArrayList<>();
    nomes.add("Somas");
    nomes.add("Subtracoes");
    nomes.add("Divisoes");
    nomes.add("Multiplicacoes");
    nomes.add("Texto");
    nomes.add("Excel");
    nomes.add("Word");
    nomes.add("Paint");
    nomes.add("Leitura");
    nomes.add("Programacao");
    for(int i = 0; i<10; i++){
      servidores.put(nomes.get(i),new Informacao());
    }
    ServerSocket ls = new ServerSocket(1234); // estou a escuta de clientes
    try{
      while(true){
         Socket cs = ls.accept(); // cliente ligou-se
         System.out.println("Cliente ligou-se");
           // envio do cliente para uma thread de autenticação
         threadAutentica ta = new threadAutentica(cs,servidores,clientes);
         ta.start();
     } 
    } catch(IOException e){
          System.out.println("Exception caught");
    } finally{
          ls.close();
    }
  }
}