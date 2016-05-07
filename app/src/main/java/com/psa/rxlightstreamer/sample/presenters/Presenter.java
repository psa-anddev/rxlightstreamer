package com.psa.rxlightstreamer.sample.presenters;

import rx.Observable;

/**
 * <p>This class provides an interface to implement
 * different presenters.</p>
 * <p>The presenter needs to be associated with a view
 * but, in this case, the view doesn't necessary have to
 * be an Android view object. Presenters can be used with
 * activities or fragments, so the type of the view can
 * be any object.</p>
 *
 * <p>The way the presenter communicates with the view
 * is through an observable. The presenter pushes events
 * through the observable. The view subscribes to these
 * events in order to </p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public interface Presenter<V, I> {
    /**
     * <p>Sets the view for the presenter.</p>
     * @param view is the ui object this presenter interacts with.
     */
    void setView(V view);

    /**
     * <p>Returns the observable to use by the presented
     * view.</p>
     * @return the observable that will cause the view to update.
     */
    Observable<I> getItemsObservable();
}
