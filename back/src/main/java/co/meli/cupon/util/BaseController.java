package co.meli.cupon.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public abstract class BaseController {

  protected static final String PARAM_PAGE_PAGE = "page";
  protected static final String PARAM_PAGE_ROWS = "rows";
  protected static final String PARAM_PAGE_FIELD = "field";
  protected static final String PARAM_PAGE_DIRECTION = "direction";
  protected static final String SORT_DESCENDING = "desc";

  public BaseController() {}

  protected Pageable parsePageParams(Map<String, String> allParams) {
    String pPage = allParams.get(PARAM_PAGE_PAGE);
    String pRows = allParams.get(PARAM_PAGE_ROWS);
    String pField = allParams.get(PARAM_PAGE_FIELD);
    String pDirection = allParams.get(PARAM_PAGE_DIRECTION);

    Sort.Order order = null;
    if (pField != null) {
      if (SORT_DESCENDING.equalsIgnoreCase(pDirection)) {
        order = Sort.Order.desc(pField);
      } else {
        order = Sort.Order.asc(pField);
      }
    }

    Sort sort = order != null ? Sort.by(order) : Sort.unsorted();
    Pageable pageable;
    if (StringUtils.isNumeric(pRows)) {
      if (StringUtils.isNumeric(pPage)) {
        pageable = PageRequest.of(Integer.parseInt(pPage), Integer.parseInt(pRows), sort);
      } else {
        pageable = PageRequest.of(0, Integer.parseInt(pRows), sort);
      }
    } else {
      pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
    }

    return pageable;
  }
}
