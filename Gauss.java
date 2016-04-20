package ua.goit.gauswithhgui;

public interface Gauss<N extends Number, T extends Gauss<N, T>> {
    void addEquation(T item);
    void mul(N coefficient);
    N findCoefficient(N a, N b);
    N at(int index);
    int size();
}