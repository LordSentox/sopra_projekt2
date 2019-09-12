package de.sopra.javagame.view.customcontrol;


import de.sopra.javagame.view.textures.TextureLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import jfxtras.labs.scene.control.radialmenu.RadialMenu;
import jfxtras.labs.scene.control.radialmenu.RadialMenuItem;

public class ActionPicker extends RadialMenu{
    public ActionPicker (){
        super();
        this.addMenuItem(new RadialMenuItem(5, "Bewegen", new ImageView(TextureLoader.getBackground()), new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("owo");                
            }
        }));
    }

}
