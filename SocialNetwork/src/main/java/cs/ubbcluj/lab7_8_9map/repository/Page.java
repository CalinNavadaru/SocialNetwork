package cs.ubbcluj.lab7_8_9map.repository;

public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalElementCount;

    public Page(Iterable<E> elementsOnPage, int totalElementCount) {
        this.elementsOnPage = elementsOnPage;
        this.totalElementCount = totalElementCount;
    }

    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }

    public int getTotalElementCount() {
        return totalElementCount;
    }
}