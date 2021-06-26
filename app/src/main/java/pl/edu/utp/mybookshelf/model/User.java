package pl.edu.utp.mybookshelf.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private String id;
    private String email;
    private String password; // TODO: delete password from model
    private String fullName;
}
