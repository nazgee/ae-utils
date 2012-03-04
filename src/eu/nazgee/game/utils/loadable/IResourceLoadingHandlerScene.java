package eu.nazgee.game.utils.loadable;

import org.andengine.entity.scene.Scene;

public interface IResourceLoadingHandlerScene extends IResourceLoadingHandler {
	Scene onGetScene();
}
