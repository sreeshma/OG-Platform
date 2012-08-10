package com.opengamma.integration.copiernew.portfoliocontent;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.integration.copiernew.Copier;
import com.opengamma.integration.copiernew.Writeable;
import com.opengamma.integration.copiernew.nodepositionsecurity.NodePositionSecurity;
import com.opengamma.integration.copiernew.nodepositionsecurity.NodePositionSecurityMasterWriter;
import com.opengamma.integration.copiernew.portfolio.PortfolioMasterWriter;
import com.opengamma.master.portfolio.ManageablePortfolio;
import com.opengamma.master.portfolio.PortfolioDocument;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.portfolio.PortfolioSearchRequest;
import com.opengamma.master.portfolio.PortfolioSearchResult;
import com.opengamma.master.portfolio.PortfolioSearchSortOrder;
import com.opengamma.master.position.ManageablePosition;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.security.ManageableSecurity;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.beancompare.BeanCompare;
import com.opengamma.util.beancompare.BeanDifference;
import com.opengamma.util.tuple.ObjectsPair;
import com.opengamma.util.tuple.Triple;

import javax.time.calendar.ZonedDateTime;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.List;

public class PortfolioContentMasterWriter implements Writeable<PortfolioContent> {

  private Writeable<ManageablePortfolio> _portfolioWriter;
  private PositionMaster _positionMaster;
  private Writeable<ManageableSecurity> _securityWriter;
  private BeanCompare _beanCompare;

  public PortfolioContentMasterWriter(Writeable<ManageablePortfolio> portfolioWriter, PositionMaster positionMaster,
                                      Writeable<ManageableSecurity> securityWriter) {
    ArgumentChecker.notNull(portfolioWriter, "portfolioWriter");
    ArgumentChecker.notNull(positionMaster, "positionMaster");
    ArgumentChecker.notNull(securityWriter, "securityWriter");

    _portfolioWriter = portfolioWriter;
    _positionMaster = positionMaster;
    _securityWriter = securityWriter;
    _beanCompare = new BeanCompare();
  }

  @Override
  public PortfolioContent addOrUpdate(PortfolioContent portfolioContent) {

    ArgumentChecker.notNull(portfolioContent, "portfolioContent");

    ManageablePortfolio portfolio = portfolioContent.getPortfolio();
    Iterable<NodePositionSecurity> positionReader = portfolioContent.getNodePositionSecurityReader();

    _portfolioWriter.addOrUpdate(portfolio);

    Writeable<NodePositionSecurity> positionWriter =
        new NodePositionSecurityMasterWriter(_positionMaster, _securityWriter, portfolio.getRootNode(), false);

    new Copier<NodePositionSecurity>().copy(positionReader, positionWriter);

    return portfolioContent;
  }

  @Override
  public void flush() throws IOException {
    // No action
  }
}
