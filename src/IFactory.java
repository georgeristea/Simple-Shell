public interface IFactory {

    ICommand createCommand(String input, Directory root);
}
