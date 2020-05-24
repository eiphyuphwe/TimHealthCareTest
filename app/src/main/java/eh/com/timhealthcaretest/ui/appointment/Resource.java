package eh.com.timhealthcaretest.ui.appointment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource< T > {

    public enum Status {
        ERROR,
        SUCCESS
    }

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public Resource (@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;

    }


    public static < T > Resource< T > error (@Nullable T data, @NonNull String message) {
        return new Resource<> ( Status.ERROR, data, message );
    }

    public static < T > Resource< T > success (@NonNull T data) {
        return new Resource<> ( Status.SUCCESS, data, null );
    }


}

