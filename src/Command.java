public interface Command {
    void execute();
    void undo();
    void redo();
    String commandAction();
}
