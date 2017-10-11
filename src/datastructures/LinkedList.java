package datastructures;

/**
 *
 * @author Dylan
 */
public class LinkedList
{
    private Node head;
    
    public LinkedList()
    {
        head = null;
    }
    
    public boolean isEmpty()
    {
        return (head == null);
    }

    public void insert(Node n) 
    {
        n.next = head;
        head = n;
    }

    public Node delete()
    {
        Node n = head;
        if(head == null)
        {
            return null;
        }
        head = head.next;
        return n;
    }
    
    public Node getHead()
    {
        return head;
    }
    
    public String printEventList()
    {
        StringBuilder str = new StringBuilder();
        Node n = head;
        while (n != null) 
        {
            str.append(n.printNode()).append(" , ");
            n = n.next;
        }
        return str.toString();
    }
}
