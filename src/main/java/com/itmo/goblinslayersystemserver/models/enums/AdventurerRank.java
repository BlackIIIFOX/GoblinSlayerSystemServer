package com.itmo.goblinslayersystemserver.models.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AdventurerRank {
     Platinum(10000),
     Gold(5000),
     Silver(1000),
     Bronze(500),
     Ruby(200),
     Emerald(100),
     Sapphire(50),
     Steel(10),
     Obsidian(5),
     Porcelain(0);

     /**
      * Требуемое количество опыта для ранга (столбец adventurer_experience в БД)
      */
     @Getter
     private final Integer experienceRequired;

     AdventurerRank(Integer experienceRequired) {
          this.experienceRequired = experienceRequired;
     }

     /**
      * Сравнивает два ранга на равенство и меньшинство.
      * @param rank ранг-параметр.
      * @return Возвращает true, если ранг меньше по приоритету, либо равен рангу-параметру.
      */
     public boolean IsLessOrEqual(AdventurerRank rank) {
          return this.getExperienceRequired() <= rank.getExperienceRequired();
     }

     /**
      * Возвращает ранги, которые являются меньше, чем передаваемый ранг (сам ранг тоже входит в коллекцию).
      * @param rank анализируемый ранг.
      * @return Коллекция рангов, которые меньше, либо равны по приоритету.
      */
     public static List<AdventurerRank> GetRanksThatLessOrEqual(AdventurerRank rank) {
          List<AdventurerRank> result = new ArrayList<>();

          result.add(rank);
          Arrays.stream(AdventurerRank.values()).forEach(adventurerRank -> {
               if (!rank.IsLessOrEqual(adventurerRank)) {
                    result.add(adventurerRank);
               }
          });

          return result;
     }
}
