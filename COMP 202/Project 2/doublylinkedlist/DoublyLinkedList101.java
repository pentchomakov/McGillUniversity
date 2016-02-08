public class DoublyLinkedList{
  private Node first;
  private Node last;
  private int length;
  
  public DoublyLinkedList(){
    this.first = null;
    this.length = 0;
  }
  
  public int getLength(){
    return this.length;
  }
  
  // find if the value exists in the list
  // if it does, return the first node that matches
  // return null otherwise
  public Node find(String value){
    Node n = this.first;
    while(n.getData() != value){
      n=n.getNext();
    }
    if(n.getData() == value){
      return n;
    }
    return null;
  }
  
// insert a new node at the beginning of the list
  public void insertStart(String value){
    this.length++;
    Node n = new Node(value);
    Node m = this.first;
    if(m == null){
      this.first = n;
      this.last = n;
    }
    else{
      n.setNext(m);
      m.setPrev(n);
      this.first = n;
    }
  }
  
// insert a new node at the end of the list
  public void insertEnd(String value){
    this.length++;
    Node n = new Node(value);
    if (this.first == null){
      this.first = n;
      this.last = n;
    }
    else{
      this.last.setNext(n);
      n.setPrev(this.last);
      this.last = n;
    }
  }
  
  // remove all the occurences of the value in the list
  public void remove(String value){
    Node n = this.first;
    if(n == null){
      System.out.println("This is an empty list.");
      return;
    }
    while(n.getData() != value){
      if (n.getNext() == null){
        return;
      }
      n=n.getNext();
    }
    
    Node a = n.getPrev();
    Node b = n.getNext();
    a.setNext(b);
    b.setPrev(a);
    this.length--;
    this.remove(value);
  }
  

  // remove from the list the Node at the position given
  // by the value of index.
  public void removeAtIndex(int index){
    if(index > this.length-1){
      System.out.println("Please select a smaller index");
      return;
    }
    Node n = this.first;
    if(n == null){
      System.out.println("This is an empty list.");
      return;
    }
    else{
      this.length--;
      for(int i=0; i<index; i++){
        n=n.getNext();
      }
      Node m = n.getPrev();
      m.setNext(n.getNext());
    }
  }
  
  // print the string in reverse order
  public String toStringReverse(){
    String result = "";
    for(int i=0; i<this.length; i++){
      Node n = this.first;
      for (int j=0; j<this.length-i-1; j++ ){
        n=n.getNext();
      }
      result = (result + n.getData() + " ");
    }
    return result;
  }
  
  // print the string
  public String toString(){
    String str = "";
    Node pointer = this.first;
    while ( pointer != null ) {
      str += pointer.getData();
      pointer = pointer.getNext();
    }        
    return str;
  }
  
  public static void main (String[] args){
    DoublyLinkedList list = new DoublyLinkedList();
    String once = "And you may find yourself ";
    
    list.insertStart("I am helpless. ");
    list.insertEnd(once);
    list.insertEnd("I do not believe ");
    list.insertEnd("Hello!. ");
    list.insertEnd("There is hope. ");
    
    Node n = list.find(once);
    n.setData(n.getData() + " in a shotgun shack.");
    
    System.out.println(list);
    
    list.remove(once);
    list.removeAtIndex(4);
    
    System.out.println(list);
    
    System.out.println(list.toStringReverse());
  }
}
