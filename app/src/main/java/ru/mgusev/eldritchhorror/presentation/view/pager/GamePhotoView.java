package ru.mgusev.eldritchhorror.presentation.view.pager;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.mgusev.eldritchhorror.strategy.DismissDialogStrategy;

public interface GamePhotoView extends MvpView {

    String IMAGE_VIEWER_TAG = "fullScreenImageViewer";
    String DELETE_DIALOG_TAG = "deleteDialog";

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updatePhotoGallery(List<String> imagesUrlList);

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = DELETE_DIALOG_TAG)
    void showDeleteDialog();

    @StateStrategyType(value = DismissDialogStrategy.class, tag = DELETE_DIALOG_TAG)
    void hideDeleteDialog();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void dispatchTakePictureIntent();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectGalleryItem(List<String> list, int position);

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = IMAGE_VIEWER_TAG)
    void openFullScreenPhotoViewer();

    @StateStrategyType(value = DismissDialogStrategy.class, tag = IMAGE_VIEWER_TAG)
    void closeFullScreenPhotoViewer();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openImagePicker();
}
