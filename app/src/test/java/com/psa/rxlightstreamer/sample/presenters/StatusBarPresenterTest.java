package com.psa.rxlightstreamer.sample.presenters;

import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.sample.BaseTest;
import com.psa.rxlightstreamer.sample.R;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.ui.fragments.StatusFragment;

import org.junit.Test;
import org.mockito.Mock;

import rx.observers.TestSubscriber;
import rx.subjects.BehaviorSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Feature: As a developer I want to have a presenter
 * to update the status bar.</p>
 */
public class StatusBarPresenterTest extends BaseTest
{
    private StatusBarPresenter mStatusBarPresenter;
    @Mock
    private ServiceMediator mServiceMediator;
    @Mock
    private StatusFragment mStatusFragment;
    private TestSubscriber<Integer> mTestSubscriber;


    @Override
    public void setUp()
    {
        super.setUp();
        mStatusBarPresenter = new StatusBarPresenter(mServiceMediator);
        mTestSubscriber = new TestSubscriber<>();
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new disconnected status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsDisconnectedStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.DISCONNECTED));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.disconnected);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new will retry status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsWillRetryStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.WILL_RETRY));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.will_retry);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new connecting status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsConnectingStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.CONNECTING));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.connecting);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new stream sensing status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsStreamSensingStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.STREAM_SENSING));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.stream_sensing);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new http polling status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsHttpPollingStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.HTTP_POLLING));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.http_polling);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new HTTP streaming status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsHttpStreamingStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.HTTP_STREAMING));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.http_streaming);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new web socket streaming status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsWSStreamingStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.WS_STREAMING));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.ws_streaming);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new web socket polling status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsWSPollingStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.WS_POLLING));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.ws_polling);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario:Presenter sends the right string for the status to the fragment.</p>
     * <p>Given I am seeing the status fragment
     * When a new stalled status is received
     * Then the presenter sends the right string to the fragment.</p>
     */
    @Test
    public void testPresenterSendsStalledStatusToFragment()
    {
        try
        {
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create(ClientStatus.STALLED));
            mStatusBarPresenter.setView(mStatusFragment);
            mStatusBarPresenter.getItemsObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(R.string.stalled);
            verify(mServiceMediator).getClientStatusSubject();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
