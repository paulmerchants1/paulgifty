package com.auth.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GiftyAPIException extends RuntimeException {



    public GiftyAPIException(String message) {
        super(message);
    }
}