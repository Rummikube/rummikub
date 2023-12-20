package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LayeredPaneUtils {
    public static List<Component> getAllComponentsAt(JLayeredPane layeredPane, Point p) {
        List<Component> components = new ArrayList<>();
        System.out.println(p);
        for (int i = 0; i < layeredPane.getComponentCount(); i++) {
            Component comp = layeredPane.getComponent(i);
            if (comp.getBounds().contains(p)) {
                components.add(comp);
            }
        }
        return components;
    }
}