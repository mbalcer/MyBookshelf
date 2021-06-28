package pl.edu.utp.mybookshelf.model;

public enum BookState {

    TO_READ("Do przeczytania", 1),
    READ("Przeczytane", 2);

    public final String title;
    public final Integer order;

    BookState(String title, Integer order) {
        this.title = title;
        this.order = order;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
