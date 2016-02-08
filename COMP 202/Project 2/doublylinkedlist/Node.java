import java.util.*;
import java.io.*;

public class Node{
   private String data;
   private Node next;
   private Node prev;

   //Our constructor
   public Node(String input_data){
        this.data = input_data;
        this.prev = null;
        this.next = null;
   }
   
   //Data getter
   public String getData(){
	   return this.data;
   }
   //Data setter
   public void setData(String input_data){
	   this.data = input_data;
   }
   
   //Node next getter
   public Node getNext(){
	   return this.next;
   }
   //Node next setter
   public void setNext(Node n){
	   this.next = n;
   }
   
   //Node prev getter
   public Node getPrev(){
	   return this.prev;
   }
   //Node prev setter
   public void setPrev(Node n){
	   this.prev = n;
   }
}
