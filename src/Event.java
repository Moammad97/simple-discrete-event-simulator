import java.util.*;

class Event
{
    private Signal siganl;
    private int time;
    private int value;
    public Event(){}
    public Event(Signal signal, int time, int value)
    {
        setSignal(signal);
        setTime(time);
        setValue(value);
    }
    public void setSignal(Signal signal)
    {
        this.siganl = signal;
    }
    public void setTime(int time)
    {
        this.time = time;
    }
    public Signal getSignal()
    {
        return this.siganl;
    }
    public int getTime()
    {
        return this.time;
    }
    public void setValue(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return this.value;
    }
}
class EventComparator implements Comparator<Event>
{
    public int compare(Event event1, Event event2)
    {
        if (event1.getTime() < event2.getTime())
            return -1;
        else //if (event1.getTime() < event2.getTime())
            return 1;
        //return 0;
    }
}