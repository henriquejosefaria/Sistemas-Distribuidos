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

public class GestorReservas{

	  public ReentrantLock l = new ReentrantLock();
	  int nReserva;

	  public GestorReservas(){
	  	nReserva = 1;
	  }

	  public int getNReserva(){
	  	this.l.lock();
	  	try{

	  		return this.nReserva;

    	} finally{
      		this.l.unlock();
     	}
 	}

 	public void incNReserva(){
 		this.l.lock();
	  	try{

	  		this.nReserva ++;

    	} finally{
      		this.l.unlock();
     	}
 	}

}