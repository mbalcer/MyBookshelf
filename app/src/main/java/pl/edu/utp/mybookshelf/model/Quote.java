package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quote implements Serializable {

    private User user;
    private String text;
    private String page;

}
