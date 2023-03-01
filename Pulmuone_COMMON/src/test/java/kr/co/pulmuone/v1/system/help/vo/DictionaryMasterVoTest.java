package kr.co.pulmuone.v1.system.help.vo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import lombok.Getter;
import org.junit.jupiter.api.Test;

class DictionaryMasterVoTest {

    @Test
    void name() {
        Board board = new Board(12L, "kil");
        board = null;

        boolean present = Optional.ofNullable(board)
            .filter(o -> o.getId() == 11L).isPresent();
        System.out.println(present);


        new Board(12L, "kil");

        new Board(12L, "kil");
        new Board(12L, "kil");
    }


    @Getter
    class Board {
        private Long id;
        private String name;

        public Board(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}