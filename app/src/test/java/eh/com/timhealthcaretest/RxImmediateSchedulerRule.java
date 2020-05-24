package eh.com.timhealthcaretest;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxImmediateSchedulerRule implements TestRule {

    @Override
    public Statement apply (final Statement base, Description description) {
        return new Statement () {
            @Override
            public void evaluate ( ) throws Throwable {

                RxJavaPlugins.setIoSchedulerHandler ( (Function< ? super Scheduler, ? extends Scheduler >) Schedulers.trampoline () );
                RxJavaPlugins.setComputationSchedulerHandler ( (Function< ? super Scheduler, ? extends Scheduler >) Schedulers.trampoline () );
                RxJavaPlugins.setNewThreadSchedulerHandler ( (Function< ? super Scheduler, ? extends Scheduler >) Schedulers.trampoline () );
                RxAndroidPlugins.setInitMainThreadSchedulerHandler ( (Function< Callable< Scheduler >, Scheduler >) Schedulers.trampoline () );

                try
                {
                    base.evaluate ();
                }finally {
                    RxJavaPlugins.reset ();
                    RxAndroidPlugins.reset ();
                }
            }
        };
    }
}
