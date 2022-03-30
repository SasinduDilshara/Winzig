package code_generator;

import abstract_machine.AttributeError;
import parser.TreeNode;

import java.util.ArrayList;

public class CodeGeneratorHelper {
    public static ArrayList<AttributeError> checkNodeAttributeType(TreeNode node, String datatype) {
        ArrayList<AttributeError> errors = new ArrayList<>();
        for (TreeNode child: node.getChildren()) {
            if (!child.getType().equals(datatype)) {
                errors.add(AttributeError.generateError(child.getName(), node.getName(), datatype, child.getType()));
            }
        }
        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }

    public static ArrayList<AttributeError> checkNodeAttributeType(TreeNode node, String datatype1, String datatype2) {
        ArrayList<AttributeError> errors = new ArrayList<>();
        for (TreeNode child: node.getChildren()) {
            if (!(child.getType().equals(datatype1) || child.getType().equals(datatype2))) {
                errors.add(AttributeError.generateError(child.getName(), node.getName(), datatype1, datatype2, child.getType()));
            }
        }
        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }
}
