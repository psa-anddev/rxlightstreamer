package com.psa.rxlightstreamer.sample.presenters;

import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.sample.R;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.ui.fragments.StatusFragment;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.Subscriptions;

/**
 * <p>This presenter will update the status bar which is
 * a fragment inside the application.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class StatusBarPresenter implements Presenter<StatusFragment, Integer>
{
    ServiceMediator mServiceMediator;
    Subscription mSubscription;
    @SuppressWarnings("FieldCanBeLocal")
    private StatusFragment mStatusFragment;
    private BehaviorSubject<Integer> mIntegerBehaviorSubject;

    /**
     * <p>Instantiates a status bar presenter. Since the
     * presenter is to be injected, the default scope is
     * used.</p>
     */
    @Inject
    public StatusBarPresenter(ServiceMediator serviceMediator)
    {
        mServiceMediator = serviceMediator;
        mSubscription = Subscriptions.empty();
        mIntegerBehaviorSubject = BehaviorSubject.create();
    }

    @Override
    public void setView(StatusFragment view)
    {
        mStatusFragment = view;
        if (mStatusFragment == null)
            mSubscription.unsubscribe();
        else
            mSubscription = mServiceMediator.getClientStatusSubject().subscribe(
                    clientStatus -> {
                        mIntegerBehaviorSubject.onNext(getResourceIdForStatus(clientStatus));
                    }
            );
    }

    @Override
    public Observable<Integer> getItemsObservable()
    {
        return mIntegerBehaviorSubject.asObservable();
    }

    /**
     * <p>Converts the status to a string resource.</p>
     *
     * @param status is the status.
     * @return the string id associated with the status.
     */
    private int getResourceIdForStatus(ClientStatus status)
    {
        Integer resource = null;
        switch (status)
        {
            case DISCONNECTED:
                resource = R.string.disconnected;
                break;
            case WILL_RETRY:
                resource = R.string.will_retry;
                break;
            case CONNECTING:
                resource = R.string.connecting;
                break;
            case STALLED:
                resource = R.string.stalled;
                break;
            case STREAM_SENSING:
                resource = R.string.stream_sensing;
                break;
            case HTTP_POLLING:
                resource = R.string.http_polling;
                break;
            case HTTP_STREAMING:
                resource = R.string.http_streaming;
                break;
            case WS_POLLING:
                resource = R.string.ws_polling;
                break;
            case WS_STREAMING:
                resource = R.string.ws_streaming;
                break;
            default:
                break;
        }
        return resource;
    }
}
