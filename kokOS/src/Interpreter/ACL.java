package Interpreter;
import java.util.LinkedList;
import java.util.List;


public class ACL {

    public String Name; //nazwa pliku
    public static List<ACL> Acl_list = new LinkedList<ACL>(); //lista z uprawnieniami
    public boolean Read;
    public boolean Write;
    public Options Op;

    ACL(String Name) //ACL wlasciciela
    {
        this.Name = Name;
        Read = true;
        Write = true;
    }

    public enum Options
    {
        Readable,
        Unreadable,
        Written,
        Unwritten
    }}