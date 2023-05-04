package com.example.learnspring.models;

import lombok.*;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String body;

    // Post. Mapping: Many to One. Comments -> Post.
    @ManyToOne
    @NonNull
    private Post post;


}
