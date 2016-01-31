package kamil.rodzik;

/**
 * Created by Kamil on 28.01.2016.
 * Class created for sending progress from JSONListAdapter to MainActivity progress bar.
 */
public class ProgressStatus {
    public interface OnProgressBarStatusListener {
        void progressChanged();
    }

    private static ProgressStatus mInstance;
    private OnProgressBarStatusListener mListener;
    private int mState;

    private ProgressStatus() {}

    public static ProgressStatus getProgressStatusInstance() {
        if(mInstance == null) {
            mInstance = new ProgressStatus();
        }
        return mInstance;
    }

    public void setListener(OnProgressBarStatusListener listener) {
        mListener = listener;
    }

    public void changeProgress(int state) {
        if(mListener != null) {
            mState = state;
            notifyProgressChange();
        }
    }

    public int getProgress() {
        return mState;
    }

    private void notifyProgressChange() {
        mListener.progressChanged();
    }
}

