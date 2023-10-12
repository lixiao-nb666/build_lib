package event;



import java.util.ArrayList;
import java.util.List;


public class BaseEventSubscriptionSubject implements BaseEventSubject {

    private List<BaseEventObserver> observers;
    private static BaseEventSubscriptionSubject subscriptionSubject;

    private BaseEventSubscriptionSubject() {
        observers = new ArrayList<>();
    }

    public static BaseEventSubscriptionSubject getInstence() {
        if (subscriptionSubject == null) {
            synchronized (BaseEventSubscriptionSubject.class) {
                if (subscriptionSubject == null)
                    subscriptionSubject = new BaseEventSubscriptionSubject();
            }
        }
        return subscriptionSubject;

    }

    @Override
    public void attach(BaseEventObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(BaseEventObserver observer) {
        observers.remove(observer);
    }


}
