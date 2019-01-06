import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;


class ClienteServidor {
	public static void main(String[] args) {
		 String linha;
         boolean b = true;
         Scanner s = new Scanner(System.in);
         try{
            //estableço conecção ao server
            Socket cs = new Socket("localhost", 1234);
            while(b){
            	 // ler o que o server escreve
                 BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                 try{// faz escrita e flush automágicamente
                 	PrintWriter pw = new PrintWriter(cs.getOutputStream(),true);
                 	while(b){ // criação de info a reenviar
                       // br.lines().forEach(p -> System.out.println(p));
                        while(true){
                            linha = br.readLine();
                            if(linha.equals("fim")) break;
                            System.out.println(linha);
                        }
                        pw.println(s.nextLine());
                 	}
                 } catch(java.lang.NullPointerException e){
                    System.out.println("Foi desconectado!");
                 }catch (java.util.NoSuchElementException e){
                    cs.shutdownOutput(); 
                    System.out.println("Foi desconectado!");
                 } catch(IOException e){
                    cs.shutdownOutput(); 
                    System.out.println("Foi desconectado!");
                 }
                 finally{
                 	cs.close();
                 }
            }
        } catch (java.util.NoSuchElementException e){
             System.out.println("Foi desconectado!");
        } catch(IOException e){
             System.out.println("");
         } finally {
         	s.close();
        }
	}
}