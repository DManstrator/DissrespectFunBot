package tk.dmanstrator.discordbots.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

public class EventWaiter implements EventListener {
    
    @SuppressWarnings("rawtypes")
    List<WaitedEvent> waitlist = new ArrayList<>();
    
    private class WaitedEvent<T extends Event> {
        private final Class<T> classToWait;
        private final Predicate<T> condition;
        private final Consumer<T> function;
        
        public WaitedEvent(Class<T> classToWait, Predicate<T> condition, Consumer<T> function) {
            this.classToWait = classToWait;
            this.condition = condition;
            this.function = function;
        }
        
        public Class<T> getClassToWait() {
            return classToWait;
        }

        public boolean tryExecuting(T otherEvent)  {
            if (classToWait.equals(otherEvent.getClass()))  {
                if (condition.test(otherEvent))  {
                    function.accept(otherEvent);
                    return true;
                }
            }
            return false;
        }
    }
    
    public <T extends Event> void waitForEvent(Class<T> classToWait, Predicate<T> condition, Consumer<T> function)  {
        WaitedEvent<T> waitedEvent = new WaitedEvent<>(classToWait, condition, function);
        waitlist.add(waitedEvent);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void onEvent(Event event) {
        WaitedEvent wEvent = new ArrayList<>(waitlist).stream()
                .filter(waitedEvent -> waitedEvent.getClassToWait() == event.getClass())
                .filter(waitedEvent -> waitedEvent.tryExecuting(event))
                .findFirst().orElse(null);
        if (wEvent != null)  {
            waitlist.remove(wEvent);
        }
    }
}