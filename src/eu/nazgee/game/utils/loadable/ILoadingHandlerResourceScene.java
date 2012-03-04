package eu.nazgee.game.utils.loadable;

import org.andengine.entity.scene.Scene;

public interface ILoadingHandlerResourceScene extends ILoadingHandlerResource {
	Scene onGetScene();
}
