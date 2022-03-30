package abstract_machine;

public class AttributeError {
    private String nextNode;
    private String node;
    private String expectedType;
    private String foundType;
    private String message;

    public AttributeError(String node, String nextNode, String expectedType, String foundType) {
        this.nextNode = nextNode;
        this.node = node;
        this.expectedType = expectedType;
        this.foundType = foundType;
        setMessage(generateErrorMessage(node, nextNode, expectedType, foundType));
    }

    public static AttributeError generateError(String node, String nextNode, String expectedType, String foundType) {
        return new AttributeError(node, nextNode, expectedType, foundType);
    }

    public static AttributeError generateError(String node, String nextNode, String expectedType1, String expectedType2, String foundType) {
        return new AttributeError(node, nextNode, expectedType1 + " Or " + expectedType2, foundType);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String generateErrorMessage(String node, String nextNode, String expectedType, String foundType) {
        return expectedType + " expected in " + node + ". But Found " + foundType;
    }
}
