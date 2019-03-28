import java.util.*;
public class OrderedList<T extends Comparable<T>> extends LinkedList<T> {

    private static final long serialVersionUID = 1L;


    public boolean orderedAdd(T element) {      
        ListIterator<T> itr = listIterator();
        while(true) {
            if (itr.hasNext() == false) {
                itr.add(element);
                return(true);
            }

            T elementInList = itr.next();
            if (elementInList.compareTo(element) > 0) {
                itr.previous();
                itr.add(element);
                System.out.println("Adding");
                return(true);
            }
        }
    }
}