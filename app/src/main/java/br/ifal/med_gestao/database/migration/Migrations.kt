package br.ifal.med_gestao.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val sql = """ 
            CREATE TABLE IF NOT EXISTS `patient` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT,
            `name` TEXT NOT NULL,
            `email` TEXT NOT NULL,
            `cpf` TEXT,
            `birth_date` TEXT,
            `sex` TEXT,
            `telephone` TEXT,
            `password` TEXT NOT NULL
        );
        """

        database.execSQL(sql)
    }
}

val MIGRATION_2_3 = object: Migration(1, 2){
    override fun migrate(database: SupportSQLiteDatabase) {
        val sql = """
            alter table `Doctor` 
            add column `price` NUMERIC;
        """

        database.execSQL(sql)
    }

}

//val MIGRATION_3_4 = object: Migration(2, 3){
//    override fun migrate(database: SupportSQLiteDatabase) {
//        val sql = """
//            ALTER TABLE `patient`
//            ADD COLUMN `active` INTEGER;
//
//            ALTER TABLE `patient`
//            ADD COLUMN `logged` INTEGER;
//        """
//    }
//}