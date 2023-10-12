package event;






public interface BaseEventSubject {
    /**
     * 增加订阅者
     *
     * @param observer
     */
    public void attach(BaseEventObserver observer);

    /**
     * 删除订阅者
     *
     * @param observer
     */
    public void detach(BaseEventObserver observer);

}
