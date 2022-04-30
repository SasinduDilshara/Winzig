package code_generator;

import abstract_machine.AttributeError;
import constants.StackConstants;
import exceptions.InvalidIdentifierException;
import parser.TreeNode;

import java.util.ArrayList;

public class CodeGeneratorHelper {
    public static ArrayList<AttributeError> checkNodeAttributeType(TreeNode node, String datatype, DclnTable dclnTable, boolean isLocal) {
        ArrayList<AttributeError> errors = new ArrayList<>();
        for (TreeNode child: node.getChildren()) {
            if (child.getType().equals(StackConstants.DataTypes.Identifier)) {
                try {
                    child.setType(getTypeOfIdentifier(child, dclnTable, isLocal));
                } catch (Exception e) {
                    errors.add(new AttributeError(e.getMessage()));
                }
            }
            if (!child.getType().equals(datatype)) {
                errors.add(AttributeError.generateError(child.getName(), node.getName(), datatype, child.getType()));
            }
        }
        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }

    public static ArrayList<AttributeError> checkNodeAttributeType(TreeNode node, String datatype1, String datatype2, DclnTable dclnTable, boolean isLocal) {
        ArrayList<AttributeError> errors = new ArrayList<>();
        for (TreeNode child: node.getChildren()) {
            if (child.getType().equals(StackConstants.DataTypes.Identifier)) {
                try {
                    child.setType(getTypeOfIdentifier(child, dclnTable, isLocal));
                } catch (Exception e) {
                    errors.add(new AttributeError(e.getMessage()));
                }
            }
            if (!(child.getType().equals(datatype1) || child.getType().equals(datatype2))) {
                errors.add(AttributeError.generateError(child.getName(), node.getName(), datatype1, datatype2, child.getType()));
            }
        }
        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }

    private static String getTypeOfIdentifier(TreeNode child, DclnTable dclnTable, boolean isLocal) throws Exception {
        DclnRow dclnRow = dclnTable.lookup(child.getLastChild().getName(), isLocal);
        if (dclnRow == null) {
            throw new InvalidIdentifierException(InvalidIdentifierException.generateErrorMessage(child.getName()));
        } else {
            return dclnRow.getType();
        }
    }
}
