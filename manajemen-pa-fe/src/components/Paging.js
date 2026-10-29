import React, {useEffect} from 'react'

export const Paging = ({
    page,
    perPage,
    total,
    onPageChange
}) => {

    useEffect(() => {
    }, []);

    const totalPage = () => {
        if (perPage >= total) {
            return 0;
        } else {
            return Math.ceil(total / perPage)
        }
    }

    const getStartAt = () => {
        var currentPage = page + 1;
        if (currentPage === 1) {
            return 1
        } else {
            return (currentPage - 1 < 1 ? 1 : ((currentPage - 1) * perPage))
        }
    }

    const getEndAt = () => {
        var currentPage = page + 1;
        if (perPage >= total) {
            return total;
        } else {
            var currentEnd = ((currentPage * perPage) > total ? total : (currentPage * perPage))
            return (currentPage < 1 ? total : currentEnd);
        }
    }

    const paginateFirstItem = () => {
        if (page <= 3) {
            return 0;
        } else {
            return page - 3
        }
    }
    const paginateLastItem = () => {
        const total =  totalPage()
        if (page  + 4 >=  total) {
            return total
        }

        return page + 4
    }


    return <div className="row">
        {/*<div className="col-sm-9 col-xs-12 col-md-8">*/}
        {/*    */}
        {/*    {(totalPage() > 0) &&*/}
        {/*        <ul className="pagination pagination-sm">*/}
        {/*            <li className={page === 0 ? "disabled" : ""}>*/}
        {/*                <a href="#" onClick={() => {*/}
        {/*                    if (page === 0) {*/}
        {/*                        return*/}
        {/*                    }*/}
        {/*                    onPageChange({*/}
        {/*                        page: 0*/}
        {/*                    })*/}
        {/*                }}>Pertama</a>*/}
        {/*            </li><li className={page === 0 ? "disabled" : ""}>*/}
        {/*                <a href="#" onClick={() => {*/}
        {/*                    if (page === 0) {*/}
        {/*                        return*/}
        {/*                    }*/}
        {/*                    onPageChange({*/}
        {/*                        page: page - 1*/}
        {/*                    })*/}
        {/*                }}>Sebelumnya</a>*/}
        {/*            </li>*/}
        {/*            {_.range(paginateFirstItem(), paginateLastItem()).map((paginationPage, index) => {*/}
        {/*                return <li className={page === paginationPage ? "active" : ""} key={index}>*/}
        {/*                    <a href="#" onClick={() => {*/}
        {/*                        onPageChange({*/}
        {/*                            page: paginationPage*/}
        {/*                        })*/}
        {/*                    }}>{paginationPage + 1}</a>*/}
        {/*                </li>*/}
        {/*            })}*/}
        {/*            <li className={totalPage() - 1 <= page ? "disabled" : ""}>*/}
        {/*                <a href="#" onClick={() => {*/}
        {/*                    if (totalPage() - 1 <= page) {*/}
        {/*                        return*/}
        {/*                    }*/}
        {/*                    onPageChange({*/}
        {/*                        page: page + 1*/}
        {/*                    })*/}
        {/*                }}>Berikutnya</a>*/}
        {/*            </li>*/}
        {/*            <li className={totalPage() - 1 <= page ? "disabled" : ""}>*/}
        {/*                <a href="#" onClick={() => {*/}
        {/*                    if (totalPage() - 1 <= page) {*/}
        {/*                        return*/}
        {/*                    }*/}
        {/*                    onPageChange({*/}
        {/*                        page: totalPage() -1*/}
        {/*                    })*/}
        {/*                }}>Terakhir</a>*/}
        {/*            </li>*/}
        {/*        </ul>}*/}
        {/*</div>*/}
        <div className="col-sm-3 col-xs-12 col-md-4 ">
            <div className="pagination pull-right">Menampilkan&nbsp; <strong>{getStartAt()}</strong> &nbsp; Sampai &nbsp;<strong>{getEndAt()}</strong> &nbsp;Dari&nbsp; <strong>{total}</strong>&nbsp; Data &nbsp;</div>
        </div>
    </div>
}
