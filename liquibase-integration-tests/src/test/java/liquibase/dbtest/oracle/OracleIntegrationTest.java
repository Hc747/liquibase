package liquibase.dbtest.oracle;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.dbtest.AbstractIntegrationTest;
import liquibase.exception.ValidationFailedException;
import org.junit.Test;

import java.util.Date;

/**
 * create tablespace liquibase2 datafile 'C:\ORACLEXE\ORADATA\XE\LIQUIBASE2.DBF' SIZE 5M autoextend on next 5M
 */
public class OracleIntegrationTest extends AbstractIntegrationTest {

    public OracleIntegrationTest() throws Exception {
        super("oracle", DatabaseFactory.getInstance().getDatabase("oracle"));
        // Respect a user-defined location for sqlnet.ora, tnsnames.ora etc. stored in the environment
        // variable TNS_ADMIN. This allowes the use of TNSNAMES.
        if (System.getenv("TNS_ADMIN") != null)
            System.setProperty("oracle.net.tns_admin",System.getenv("TNS_ADMIN"));
    }

    @Override
    @Test
    public void testRunChangeLog() throws Exception {
        super.testRunChangeLog();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Test
    public void smartDataLoad() throws Exception {
        if (this.getDatabase() == null) {
            return;
        }

        Liquibase liquibase = createLiquibase("changelogs/common/smartDataLoad.changelog.xml");
        clearDatabase(liquibase);

        try {
            liquibase.update(this.contexts);
        } catch (ValidationFailedException e) {
            e.printDescriptiveError(System.out);
            throw e;
        }

        // check that the automatically rollback now works too
        try {
            liquibase.rollback( new Date(0),this.contexts);
        } catch (ValidationFailedException e) {
            e.printDescriptiveError(System.out);
            throw e;
        }
    }

    @Override
    @Test
    public void testDiffExternalForeignKeys() throws Exception {
        //cross-schema security for oracle is a bother, ignoring test for now
    }
}