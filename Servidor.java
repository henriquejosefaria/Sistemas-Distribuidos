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



class Servidor{
  private int tipo; // leilão ou reserva
  private int tReserva; // tipo de reserva
  private int idCliente;

  public Servidor(){
    this.tipo = 0;
    this.tReserva = 0;
    this.idCliente = 0;
  }

  public Servidor(int x, int t, int id){
    this.tipo = x;
    this.tReserva = t;
    this.idCliente = id;
  }

  public int getTipo(){
    return this.tipo;
  }

  public int getTReserva(){
    return this.tReserva;
  }

  public int getIdCliente(){
    return this.idCliente;
  }

  public void setTReserva(int i){
    this.tReserva = i; 
  }

  public void setIdCliente(int id){
    this.idCliente = id;

  }
}
