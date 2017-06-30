package service.stage;

import javafx.concurrent.Task;

public class CountTask extends Task<Void> {

        private final String mMessage;

        public CountTask(String message) {
            mMessage = message;
        }

        @Override
        protected Void call() throws Exception {
            int max = 1500;
            for (Integer i = 1; i <= max; i++) {
                if (isCancelled()) {
                    break;
                }
                Integer j = i * 100 / max;
                updateProgress(i, max);
                updateMessage(mMessage + j.toString() + "%");

                Thread.sleep(20);
            }
            return null;
        }

    }
